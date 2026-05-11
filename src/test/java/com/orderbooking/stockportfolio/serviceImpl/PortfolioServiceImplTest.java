package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.PortfolioDto;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

    @Mock
    private HoldingRepository holdingRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private PortfolioServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PortfolioServiceImpl(holdingRepository, cacheDataMap);
    }

    @Test
    void getTraderPortFolio_buildsPositionsAndSectorBreakdown() {
        when(holdingRepository.findByTraderId(1L)).thenReturn(List.of(
                new Holding(1L, 1L, 10L, 100L, 5, new Date()),
                new Holding(2L, 1L, 11L, 100L, 7, new Date()),
                new Holding(3L, 1L, 12L, 200L, 3, new Date())
        ));
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        stockMap.put(10L, "A");
        stockMap.put(11L, "B");
        stockMap.put(12L, "C");
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(100L, "Tech");
        sectorMap.put(200L, "Finance");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);

        PortfolioDto result = service.getTraderPortFolio(1L);
        assertEquals(1L, result.getTraderId());
        assertEquals(3, result.getPositions().size());
        assertEquals(12, result.getSectorBreakDown().get("Tech"));
        assertEquals(3, result.getSectorBreakDown().get("Finance"));
    }
}
