package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.PortfolioDto;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.repository.*;
import com.orderbooking.stockportfolio.service.PortfolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioServiceImpl implements PortfolioService {


//    private final SectorRepository sectorRepository;
//    private final StockRepository stockRepository;
    private final HoldingRepository holdingRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);
    public PortfolioServiceImpl(HoldingRepository holdingRepository,CacheDataMap cacheDataMap){ //SectorRepository sectorRepository, StockRepository stockRepository, ) {
        this.holdingRepository = holdingRepository;
        this.cacheDataMap = cacheDataMap;
//        this.stockRepository = stockRepository;
//        this.sectorRepository = sectorRepository;
    }

    @Override
    public PortfolioDto getTraderPortFolio(Long traderId) {

        logger.info("Preparing portfolio info of trader {}",traderId);
        List<Holding> tradersHolding = this.holdingRepository.findByTrader_Id(traderId);
//        List<Sector> sectors = this.sectorRepository.findAll();
//        List<Stock> stocks = this.stockRepository.findAllByIdIn(tradersHolding.stream().map(Holding::getStockId).toList());

        return preparePortFolioOfTrader(tradersHolding, traderId);
    }

    private PortfolioDto preparePortFolioOfTrader( List<Holding> tradersHolding, Long traderId) {

        Map<String, Integer> positions = new HashMap<>();
        Map<String, Integer> sectorBreakDown = new HashMap<>();
        Map<Long,String> stockIdNameMap = cacheDataMap.getStockIdNameMap();

        tradersHolding.forEach(holding -> {
            String sectorName = holding.getSector() != null ? holding.getSector().getName() : "Unknown";
            String stockName = stockIdNameMap.get(holding.getStock().getId());

            positions.put(stockName,  holding.getQuantity());
            sectorBreakDown.put(sectorName, sectorBreakDown.getOrDefault(sectorName, 0) + holding.getQuantity());
        });

        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setTraderId(traderId);
        portfolioDto.setPositions(positions);
        portfolioDto.setSectorBreakDown(sectorBreakDown);

        return portfolioDto;
    }
}
