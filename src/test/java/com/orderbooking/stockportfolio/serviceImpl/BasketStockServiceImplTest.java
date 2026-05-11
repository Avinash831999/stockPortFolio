package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.entity.StockBasketMap;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.StockBasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketStockServiceImplTest {

    @Mock
    private StockBasketRepository stockBasketRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private BasketStockServiceImpl service;
    private Map<Long, String> basketMap;
    private Map<Long, String> stockMap;


    @Test
    void createBasketStockMap_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        stockMap.put(2L, "S1");
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        when(stockBasketRepository.existsByBasketIdAndStockId(1L, 2L)).thenReturn(false);
        when(stockBasketRepository.save(any(StockBasketMap.class)))
                .thenReturn(new StockBasketMap(10L, 1L, 2L, new Date(), new Date()));

        StockBasketMapDto result = service.createBasketStockMap(dto);
        assertEquals(10L, result.getId());
        assertEquals("B1", result.getBasketName());
        assertEquals("S1", result.getStockName());
    }

    @Test
    void createBasketStockMap_missingBasket_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        assertThrows(DataNotFoundException.class, () -> service.createBasketStockMap(dto));
    }

    @Test
    void createBasketStockMap_missingStock_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
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
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        stockMap.put(2L, "S1");
        when(stockBasketRepository.existsByBasketIdAndStockId(1L, 2L)).thenReturn(true);
        StockBasketMapDto dto = new StockBasketMapDto(null, 1L, null, null, 2L, null, null);
        assertThrows(DataNotFoundException.class, () -> service.createBasketStockMap(dto));
    }

    @Test
    void delete_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(stockBasketRepository.existsByBasketIdAndStockId(1L, 2L)).thenReturn(true);
        service.delete(1L, 2L);
        verify(stockBasketRepository).deleteByBasketIdAndStockId(1L, 2L);
    }

    @Test
    void delete_notFound_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(stockBasketRepository.existsByBasketIdAndStockId(1L, 2L)).thenReturn(false);
        assertThrows(DataNotFoundException.class, () -> service.delete(1L, 2L));
    }

    @Test
    void bulkAddStocksToBasket_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        basketMap.put(1L, "B1");
        stockMap.put(2L, "S2");
        stockMap.put(3L, "S3");
        when(stockBasketRepository.findByBasketId(1L)).thenReturn(List.of(new StockBasketMap(1L, 1L, 2L, new Date(), new Date())));
        when(stockBasketRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        List<StockBasketMapDto> result = service.bulkAddStocksToBasket(1L, Set.of(2L, 3L));
        assertEquals(2, result.size());
    }

    @Test
    void bulkAddStocksToBasket_invalidStock_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        stockMap.put(2L, "S2");
        assertThrows(DataNotFoundException.class, () -> service.bulkAddStocksToBasket(1L, Set.of(2L, 99L)));
    }

    @Test
    void bulkRemoveStocksFromBasket_success() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        stockMap.put(2L, "S2");
        stockMap.put(3L, "S3");
        service.bulkRemoveStocksFromBasket(1L, List.of(2L, 3L));
        verify(stockBasketRepository).deleteAllByStockIdIn(List.of(2L, 3L));
    }

    @Test
    void bulkRemoveStocksFromBasket_invalidStock_throws() {
        service = new BasketStockServiceImpl(stockBasketRepository, cacheDataMap, basketRepository);
        basketMap = new ConcurrentHashMap<>();
        stockMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        stockMap.put(2L, "S2");
        assertThrows(DataNotFoundException.class, () -> service.bulkRemoveStocksFromBasket(1L, List.of(2L, 99L)));
    }
}
