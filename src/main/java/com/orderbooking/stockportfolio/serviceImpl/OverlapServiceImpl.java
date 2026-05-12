package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.Overlap;
import com.orderbooking.stockportfolio.dto.OverlapsInfo;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.entity.StockBasketMap;
import com.orderbooking.stockportfolio.enums.RiskFactor;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.repository.StockBasketRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.service.OverlapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OverlapServiceImpl implements OverlapService {

    private final HoldingRepository holdingRepository;
    private final StockBasketRepository stockBasketRepository;
    private final StockRepository stockRepository;
    private final BasketRepository basketRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(OverlapServiceImpl.class);
    public OverlapServiceImpl(HoldingRepository holdingRepository, StockBasketRepository stockBasketRepository,
                              StockRepository stockRepository, BasketRepository basketRepository, CacheDataMap cacheDataMap) {
        this.holdingRepository = holdingRepository;
        this.stockBasketRepository = stockBasketRepository;
        this.stockRepository = stockRepository;
        this.basketRepository = basketRepository;
        this.cacheDataMap = cacheDataMap;
    }

    private List<StockBasketMap> fetchStockBaskets() {
        return stockBasketRepository.findAll();
    }

    private List<Holding> fetchTradersHolding(Long traderId) {
        return holdingRepository.findByTrader_Id(traderId);
    }

    public OverlapsInfo calculateOverlapInfo(Long traderId) {
        List<StockBasketMap> stockBaskets = fetchStockBaskets();
        if(stockBaskets.isEmpty()){
            logger.error("Unable to Provide Overlap Info as Stock Basket Map data not found");
            throw new DataNotFoundException("Unable to Provide Overlap Info as Stock Basket Map data not found");
        }
        List<Holding> tradersHolding = fetchTradersHolding(traderId);
        OverlapsInfo overlapsInfo = new OverlapsInfo();
        List<Overlap> overlaps = new ArrayList<>();

        Map<Long, String> basketNameMap = cacheDataMap.getBasketIdNameMap();

        Set<Long> baskets = cacheDataMap.getBasketIdNameMap().keySet();
        Map<Long, List<Long>> groupedBasketAll = stockBaskets.stream()
                .collect(Collectors.groupingBy(m -> m.getBasket().getId(), Collectors.mapping(m -> m.getStock().getId(), Collectors.toList())));

        baskets.forEach(basketId -> {
            List<Long> commonStocks = new ArrayList<>();
            tradersHolding.forEach(holding -> {
                if (groupedBasketAll.get(basketId) != null) {
                    if (groupedBasketAll.get(basketId).contains(holding.getStock().getId())) {
                        commonStocks.add(holding.getStock().getId());
                    }
                }
            });
            Float overlapValue =  ((2f * commonStocks.size())/(tradersHolding.size() + groupedBasketAll.get(basketId).size()))*100f;
            Overlap overlap = new Overlap(basketNameMap.get(basketId), overlapValue);
            overlaps.add(overlap);
        });
        Overlap dominantOverlapBasket = overlaps.stream().reduce((r1, r2) -> r1.getOverlap() > r2.getOverlap() ? r1 : r2).get();

        overlapsInfo.setOverlaps(overlaps);
        overlapsInfo.setDominantBasket(dominantOverlapBasket.getBasket());
        if(dominantOverlapBasket.getOverlap() > 60f){
            overlapsInfo.setRiskFlag(RiskFactor.HIGH.name());
        }
        else if(dominantOverlapBasket.getOverlap() > 40f && dominantOverlapBasket.getOverlap() < 60f){
            overlapsInfo.setRiskFlag(RiskFactor.MEDIUM.name());
        }
        else{
            overlapsInfo.setRiskFlag(RiskFactor.LOW.name());
        }
        return overlapsInfo;
    }
}
