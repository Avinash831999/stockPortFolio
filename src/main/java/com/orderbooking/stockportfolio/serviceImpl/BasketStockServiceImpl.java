package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.StockBasketMap;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.StockBasketRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.service.BasketStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasketStockServiceImpl implements BasketStockService {

    private final StockBasketRepository stockBasketRepository;
    private final BasketRepository basketRepository;
    private final StockRepository stockRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(BasketStockServiceImpl.class);

    public BasketStockServiceImpl(StockBasketRepository stockBasketRepository, CacheDataMap cacheDataMap,
                                  BasketRepository basketRepository, StockRepository stockRepository) {
        this.stockBasketRepository = stockBasketRepository;
        this.cacheDataMap = cacheDataMap;
        this.basketRepository = basketRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public StockBasketMapDto createBasketStockMap(StockBasketMapDto stockBasketMapDto) {

        if (cacheDataMap.getBasketIdNameMap().get(stockBasketMapDto.getBasketId()) == null) {
            logger.error("Basket with id {} not found.", stockBasketMapDto.getBasketId());
            throw new DataNotFoundException("Basket with id " + stockBasketMapDto.getBasketId() + " not found.");
        }
        if (cacheDataMap.getStockIdNameMap().get(stockBasketMapDto.getStockId()) == null) {
            logger.error("Stock with id {} not found.", stockBasketMapDto.getStockId());
            throw new DataNotFoundException("Stock with id " + stockBasketMapDto.getStockId() + " not found.");
        }
        String stockName = cacheDataMap.getStockIdNameMap().get(stockBasketMapDto.getStockId());
        String basketName = cacheDataMap.getBasketIdNameMap().get(stockBasketMapDto.getBasketId());

        if (stockBasketRepository.existsByBasket_IdAndStock_Id(stockBasketMapDto.getBasketId(), stockBasketMapDto.getStockId())) {
            logger.error("Stock {} already exists in basket {}.", stockName,
                    basketName);
            throw new DataNotFoundException("Stock " + stockName +
                    " already exists in basket " + basketName);
        } else {
            StockBasketMap stockBasketMap = stockBasketRepository.save(getStockBasketFromBasketStockDto(stockBasketMapDto));
            logger.info("Stock {} added to basket {} successfully.", stockName,
                    basketName);
            return getStockBasketDtoFromStockBasketMap(stockBasketMap);
        }

    }

    @Override
    @Transactional
    public void delete(Long basketId, Long stockId) {
        if (stockBasketRepository.existsByBasket_IdAndStock_Id(basketId, stockId)) {
            stockBasketRepository.deleteByBasket_IdAndStock_Id(basketId, stockId);
            logger.info("Stock with id {} removed from basket with id {} successfully.", stockId, basketId);
        } else {
            throw new DataNotFoundException("Stock with id " + stockId + " not found in basket with id " + basketId);
        }
    }


    @Override
    public List<StockBasketMapDto> bulkAddStocksToBasket(Long basketId, Set<Long> stockIds) {
        Set<Long> allStocks = cacheDataMap.getStockIdNameMap().keySet();
        List<Long> inValidStockIds = stockIds.stream().filter(id -> !allStocks.contains(id)).toList();

        if (!inValidStockIds.isEmpty()) {
            logger.info("The following stock ids are invalid {}", inValidStockIds);
            throw new DataNotFoundException("The following stock ids are invalid " + inValidStockIds);
        }

        Basket basket = basketRepository.findById(basketId).orElseThrow(() ->
                new DataNotFoundException("Basket with id " + basketId + " not found."));

        Set<Long> existingStocksInBasket = stockBasketRepository.findByBasket_Id(basketId).stream()
                .map(m -> m.getStock().getId())
                .collect(Collectors.toSet());

        Set<Long> toAdd = stockIds.stream().filter(id -> !existingStocksInBasket.contains(id)).collect(Collectors.toSet());

        List<StockBasketMap> stockBasketMapList = toAdd.stream().map(stockId -> {
            Stock stock = stockRepository.findById(stockId).orElseThrow(() ->
                    new DataNotFoundException("Stock with id " + stockId + " not found"));
            StockBasketMap map = new StockBasketMap();
            map.setBasket(basket);
            map.setStock(stock);
            map.setCreatedAt(new Date());
            map.setUpdatedAt(new Date());
            return map;
        }).toList();

        stockBasketRepository.saveAll(stockBasketMapList);
        logger.info("Stocks with ids {} added to basket with id {} successfully.", stockIds, basketId);
        return stockBasketMapList.stream().map(this::getStockBasketDtoFromStockBasketMap).toList();

    }

    @Override
    public void bulkRemoveStocksFromBasket(Long basketId, List<Long> stockIds) {
        Set<Long> allStocks = cacheDataMap.getStockIdNameMap().keySet();
        List<Long> inValidStockIds = stockIds.stream().filter(id -> !allStocks.contains(id)).toList();


        if (!inValidStockIds.isEmpty()) {
            logger.info("The following stock ids are invalid {} and cannot be added to basket ", inValidStockIds);
            throw new DataNotFoundException("The following stock ids are invalid and cannot be added to basket " + inValidStockIds);
        }

        if (!basketRepository.existsById(basketId)) {
            throw new DataNotFoundException("Basket with id " + basketId + " not found.");
        }

        stockBasketRepository.deleteByBasket_IdAndStock_IdIn(basketId, stockIds);

    }


    private StockBasketMap getStockBasketFromBasketStockDto(StockBasketMapDto stockBasketMapDto) {
        Basket basket = basketRepository.findById(stockBasketMapDto.getBasketId()).orElseThrow(() ->
                new DataNotFoundException("Basket with id " + stockBasketMapDto.getBasketId() + " not found."));
        Stock stock = stockRepository.findById(stockBasketMapDto.getStockId()).orElseThrow(() ->
                new DataNotFoundException("Stock with id " + stockBasketMapDto.getStockId() + " not found."));
        StockBasketMap stockBasketMap = new StockBasketMap();
        stockBasketMap.setBasket(basket);
        stockBasketMap.setStock(stock);
        stockBasketMap.setCreatedAt(new Date());
        stockBasketMap.setUpdatedAt(new Date());
        return stockBasketMap;
    }

    private StockBasketMapDto getStockBasketDtoFromStockBasketMap(StockBasketMap stockBasketMap) {
        StockBasketMapDto dto = new StockBasketMapDto();
        dto.setId(stockBasketMap.getId());
        dto.setBasketId(stockBasketMap.getBasket().getId());
        dto.setBasketName(cacheDataMap.getBasketIdNameMap().get(stockBasketMap.getBasket().getId()));
        dto.setStockId(stockBasketMap.getStock().getId());
        dto.setStockName(cacheDataMap.getStockIdNameMap().get(stockBasketMap.getStock().getId()));
        dto.setCreatedAt(stockBasketMap.getCreatedAt());
        dto.setUpdatedAt(stockBasketMap.getUpdatedAt());
        return dto;
    }

}
