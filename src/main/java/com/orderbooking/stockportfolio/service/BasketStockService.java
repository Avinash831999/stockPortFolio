package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.StockBasketMap;

import java.util.List;
import java.util.Set;

public interface BasketStockService {
    StockBasketMapDto createBasketStockMap(StockBasketMapDto stockBasketMap);
    void delete(Long basketId, Long stockId);
    List<StockBasketMapDto> bulkAddStocksToBasket(Long basketId, Set<Long> stockIds);
//    List<StockBasketMapDto> bulkRemoveStocksFromBasket(Long basketId, Set<Long> stockIds);
    void bulkRemoveStocksFromBasket(Long basketId, List<Long> stockIds);
//    StockBasketMap updateBasket(StockBasketMap stockBasket);
}
