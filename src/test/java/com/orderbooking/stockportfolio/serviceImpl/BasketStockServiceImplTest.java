package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.StockBasketMap;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.StockBasketRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.support.EntityTestBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketStockServiceImplTest {

    @Mock
    private StockBasketRepository stockBasketRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private BasketStockServiceImpl service;
    private Map<Long, String> basketMap;
    private Map<Long, String> stockMap;


    @Test
    void createBasketStockMap_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        stockMap.put(2L, "S1");
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        when(stockBasketRepository.existsByBasket_IdAndStock_Id(1L, 2L)).thenReturn(false);
        when(basketRepository.findById(1L)).thenReturn(Optional.of(EntityTestBuilders.basket(1L)));
        when(stockRepository.findById(2L)).thenReturn(Optional.of(EntityTestBuilders.stock(2L, 1L)));
        when(stockBasketRepository.save(any(StockBasketMap.class)))
                .thenAnswer(inv -> {
                    StockBasketMap m = inv.getArgument(0);
                    m.setId(10L);
                    return m;
                });

        StockBasketMapDto result = service.createBasketStockMap(dto);
        assertEquals(10L, result.getId());
        assertEquals("B1", result.getBasketName());
        assertEquals("S1", result.getStockName());
    }

    @Test
    void createBasketStockMap_missingBasket_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        assertThrows(DataNotFoundException.class, () -> service.createBasketStockMap(dto));
    }

    @Test
    void createBasketStockMap_missingStock_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        assertThrows(DataNotFoundException.class, () -> service.createBasketStockMap(dto));
    }

    @Test
    void createBasketStockMap_duplicate_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        stockMap.put(2L, "S1");
        when(stockBasketRepository.existsByBasket_IdAndStock_Id(1L, 2L)).thenReturn(true);
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        assertThrows(DataNotFoundException.class, () -> service.createBasketStockMap(dto));
    }

    @Test
    void delete_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        when(stockBasketRepository.existsByBasket_IdAndStock_Id(1L, 2L)).thenReturn(true);
        service.delete(1L, 2L);
        verify(stockBasketRepository).deleteByBasket_IdAndStock_Id(1L, 2L);
    }

    @Test
    void delete_notFound_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        when(stockBasketRepository.existsByBasket_IdAndStock_Id(1L, 2L)).thenReturn(false);
        assertThrows(DataNotFoundException.class, () -> service.delete(1L, 2L));
    }

    @Test
    void bulkAddStocksToBasket_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        stockMap.put(2L, "S2");
        stockMap.put(3L, "S3");
        Basket basket = EntityTestBuilders.basket(1L);
        when(basketRepository.findById(1L)).thenReturn(Optional.of(basket));
        when(stockBasketRepository.findByBasket_Id(1L)).thenReturn(List.of(
                new StockBasketMap(1L, basket, EntityTestBuilders.stock(2L, 1L), new Date(), new Date())));
        when(stockRepository.findById(3L)).thenReturn(Optional.of(EntityTestBuilders.stock(3L, 1L)));
        when(stockBasketRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        List<StockBasketMapDto> result = service.bulkAddStocksToBasket(1L, Set.of(2L, 3L));
        assertEquals(1, result.size());
    }

    @Test
    void bulkAddStocksToBasket_invalidStock_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        stockMap.put(2L, "S2");
        assertThrows(DataNotFoundException.class, () -> service.bulkAddStocksToBasket(1L, Set.of(2L, 99L)));
    }

    @Test
    void bulkRemoveStocksFromBasket_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        stockMap.put(2L, "S2");
        stockMap.put(3L, "S3");
        when(basketRepository.existsById(1L)).thenReturn(true);
        service.bulkRemoveStocksFromBasket(1L, List.of(2L, 3L));
        verify(stockBasketRepository).deleteByBasket_IdAndStock_IdIn(eq(1L), eq(List.of(2L, 3L)));
    }

    @Test
    void bulkRemoveStocksFromBasket_invalidStock_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository, stockRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        stockMap.put(2L, "S2");
        assertThrows(DataNotFoundException.class, () -> service.bulkRemoveStocksFromBasket(1L, List.of(2L, 99L)));
    }
}
