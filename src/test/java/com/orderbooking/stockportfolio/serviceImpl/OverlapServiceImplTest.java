package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.OverlapsInfo;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.repository.StockBasketRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.support.EntityTestBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverlapServiceImplTest {

    @Mock
    private HoldingRepository holdingRepository;
    @Mock
    private StockBasketRepository stockBasketRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private OverlapServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OverlapServiceImpl(holdingRepository, stockBasketRepository, stockRepository, basketRepository, cacheDataMap);
    }

    @Test
    void calculateOverlapInfo_noStockBasketData_throws() {
        when(stockBasketRepository.findAll()).thenReturn(List.of());
        assertThrows(DataNotFoundException.class, () -> service.calculateOverlapInfo(1L));
    }

    @Test
    void calculateOverlapInfo_highRisk() {
        Map<Long, String> basketMap = new ConcurrentHashMap<>();
        basketMap.put(1L, "Basket1");
        basketMap.put(2L, "Basket2");
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(stockBasketRepository.findAll()).thenReturn(List.of(
                EntityTestBuilders.stockBasketMap(1L, 1L, 10L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(2L, 1L, 11L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(3L, 2L, 20L, new Date(), new Date())
        ));
        when(holdingRepository.findByTrader_Id(1L)).thenReturn(List.of(
                EntityTestBuilders.holding(1L, 1L, 10L, 100L, 5, new Date()),
                EntityTestBuilders.holding(2L, 1L, 11L, 100L, 4, new Date())
        ));

        OverlapsInfo info = service.calculateOverlapInfo(1L);
        assertEquals("Basket1", info.getDominantBasket());
        assertEquals("HIGH", info.getRiskFlag());
    }

    @Test
    void calculateOverlapInfo_mediumRisk() {
        Map<Long, String> basketMap = new ConcurrentHashMap<>();
        basketMap.put(1L, "Basket1");
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(stockBasketRepository.findAll()).thenReturn(List.of(
                EntityTestBuilders.stockBasketMap(1L, 1L, 10L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(2L, 1L, 11L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(3L, 1L, 12L, new Date(), new Date())
        ));
        when(holdingRepository.findByTrader_Id(1L)).thenReturn(List.of(
                EntityTestBuilders.holding(1L, 1L, 10L, 100L, 5, new Date()),
                EntityTestBuilders.holding(2L, 1L, 99L, 100L, 4, new Date())
        ));

        service.calculateOverlapInfo(1L);
    }

    @Test
    void calculateOverlapInfo_lowRisk() {
        Map<Long, String> basketMap = new ConcurrentHashMap<>();
        basketMap.put(1L, "Basket1");
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(stockBasketRepository.findAll()).thenReturn(List.of(
                EntityTestBuilders.stockBasketMap(1L, 1L, 10L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(2L, 1L, 11L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(3L, 1L, 12L, new Date(), new Date()),
                EntityTestBuilders.stockBasketMap(4L, 1L, 13L, new Date(), new Date())
        ));
        when(holdingRepository.findByTrader_Id(1L)).thenReturn(List.of(
                EntityTestBuilders.holding(1L, 1L, 99L, 100L, 5, new Date()),
                EntityTestBuilders.holding(2L, 1L, 98L, 100L, 4, new Date())
        ));

        OverlapsInfo info = service.calculateOverlapInfo(1L);
        assertEquals("LOW", info.getRiskFlag());
    }
}
