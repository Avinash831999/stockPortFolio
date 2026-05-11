package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.StockBasketMap;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.StockBasketRepository;
import com.orderbooking.stockportfolio.service.BasketStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketStockServiceImpl implements BasketStockService {

    private final StockBasketRepository stockBasketRepository;
    private final BasketRepository basketRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(BasketStockServiceImpl.class);

    public BasketStockServiceImpl(StockBasketRepository stockBasketRepository, CacheDataMap cacheDataMap, BasketRepository basketRepository) {
        this.stockBasketRepository = stockBasketRepository;
        this.cacheDataMap = cacheDataMap;
        this.basketRepository = basketRepository;
    }

    @Override
    public StockBasketMapDto createBasketStockMap(StockBasketMapDto stockBasketMapDto) {

        if(cacheDataMap.getBasketIdNameMap().get(stockBasketMapDto.getBasketId()) == null){
            logger.error("Basket with id {} not found.", stockBasketMapDto.getBasketId());
            throw new DataNotFoundException("Basket with id " + stockBasketMapDto.getBasketId() + " not found.");
        }
        if(cacheDataMap.getStockIdNameMap().get(stockBasketMapDto.getStockId()) == null){
            logger.error("Stock with id {} not found.", stockBasketMapDto.getStockId());
            throw new DataNotFoundException("Stock with id " + stockBasketMapDto.getStockId() + " not found.");
        }
        String stockName = cacheDataMap.getStockIdNameMap().get(stockBasketMapDto.getStockId());
        String basketName = cacheDataMap.getBasketIdNameMap().get(stockBasketMapDto.getBasketId());

        if(stockBasketRepository.existsByBasketIdAndStockId(stockBasketMapDto.getBasketId(), stockBasketMapDto.getStockId())){
            logger.error("Stock {} already exists in basket {}.",stockName ,
                    basketName);
            throw new DataNotFoundException("Stock " + stockName +
                    " already exists in basket " + basketName);
        }
        else{
            StockBasketMap stockBasketMap =  stockBasketRepository.save(getStockBasketFromBasketStockDto(stockBasketMapDto));
            logger.info("Stock {} added to basket {} successfully.", stockName,
                    basketName);
            return getStockBasketDtoFromStockBasketMap(stockBasketMap);
        }

    }

    @Override
    public void delete(Long basketId, Long stockId) {
        if(stockBasketRepository.existsByBasketIdAndStockId(basketId, stockId)) {
            stockBasketRepository.deleteByBasketIdAndStockId(basketId, stockId);
            logger.info("Stock with id {} removed from basket with id {} successfully.", stockId, basketId);
        }
        else{
            throw new DataNotFoundException("Stock with id " + stockId + " not found in basket with id " + basketId);
        }
    }


    @Override
    public List<StockBasketMapDto> bulkAddStocksToBasket(Long basketId, Set<Long> stockIds){
        Set<Long> allStocks = cacheDataMap.getStockIdNameMap().keySet();
       List<Long> inValidStockIds = stockIds.stream().filter(id -> !allStocks.contains(id)).toList();

       if(!inValidStockIds.isEmpty()){
           logger.info("The following stock ids are invalid {}", inValidStockIds);
           throw new DataNotFoundException("The following stock ids are invalid " + inValidStockIds);
       }

       Set<Long> existingStocksInBasket = stockBasketRepository.findByBasketId(basketId).stream().map(StockBasketMap::getStockId).collect(Collectors.toSet());

       existingStocksInBasket.addAll(stockIds);
       List<StockBasketMap> stockBasketMapList=
       existingStocksInBasket.stream().map(stockId -> new StockBasketMap(null, basketId, stockId, new Date(), new Date())).toList();

       stockBasketRepository.saveAll(stockBasketMapList);
       logger.info("Stocks with ids {} added to basket with id {} successfully.", stockIds, basketId);
       return stockBasketMapList.stream().map(this::getStockBasketDtoFromStockBasketMap).toList();

    }

    @Override
    public void bulkRemoveStocksFromBasket(Long basketId, List<Long> stockIds){
        Set<Long> allStocks = cacheDataMap.getStockIdNameMap().keySet();
        List<Long> inValidStockIds = stockIds.stream().filter(id -> !allStocks.contains(id)).toList();


        if(!inValidStockIds.isEmpty()){
            logger.info("The following stock ids are invalid {} and cannot be added to basket ", inValidStockIds);
            throw new DataNotFoundException("The following stock ids are invalid and cannot be added to basket " + inValidStockIds);
        }

        stockBasketRepository.deleteAllByStockIdIn(stockIds);

    }




    private StockBasketMap getStockBasketFromBasketStockDto(StockBasketMapDto stockBasketMapDto){
        StockBasketMap stockBasketMap = new StockBasketMap();
        stockBasketMap.setBasketId(stockBasketMapDto.getBasketId());
        stockBasketMap.setStockId(stockBasketMapDto.getStockId());
        stockBasketMap.setCreatedAt(new Date());
        stockBasketMap.setUpdatedAt(new Date());
        return stockBasketMap;
    }

    private StockBasketMapDto getStockBasketDtoFromStockBasketMap(StockBasketMap stockBasketMap){
        StockBasketMapDto dto = new StockBasketMapDto();
        dto.setId(stockBasketMap.getId());
        dto.setBasketId(stockBasketMap.getBasketId());
        dto.setBasketName(cacheDataMap.getBasketIdNameMap().get(stockBasketMap.getBasketId()));
        dto.setStockId(stockBasketMap.getStockId());
        dto.setStockName(cacheDataMap.getStockIdNameMap().get(stockBasketMap.getStockId()));
        dto.setCreatedAt(stockBasketMap.getCreatedAt());
        dto.setUpdatedAt(stockBasketMap.getUpdatedAt());
        return dto;
    }

//
//    @Override
//    public StockBasketMap updateBasket(StockBasketMap stockBasket) {
//        return null;
//    }
}
