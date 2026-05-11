package com.orderbooking.stockportfolio.cacheMap;

import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.TraderStatus;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheDataMapTest {

    @Mock
    private SectorRepository sectorRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private TraderRepository traderRepository;
    @Mock
    private BasketRepository basketRepository;

    private CacheDataMap cacheDataMap;

    @BeforeEach
    void setUp() {
        cacheDataMap = new CacheDataMap(sectorRepository, stockRepository, traderRepository, basketRepository);
    }

    @Test
    void init_loadsAllMapsFromRepositories() {
        Date now = new Date();
        when(sectorRepository.findAll()).thenReturn(List.of(new Sector(1L, "Tech", now, now)));
        when(stockRepository.findAll()).thenReturn(List.of(new Stock(2L, "ABC", 10f, 1L, now, now)));
        when(traderRepository.findAll()).thenReturn(List.of(
                new Trader(3L, "Alice", "a@a.com", "ABCDE1234F", TraderStatus.ACTIVE, now, now)));
        when(basketRepository.findAll()).thenReturn(List.of(new Basket(4L, "Growth", null, now, now)));

        cacheDataMap.init();

        assertEquals("Tech", cacheDataMap.getSectorIdNameMap().get(1L));
        assertEquals("ABC", cacheDataMap.getStockIdNameMap().get(2L));
        assertEquals("Alice", cacheDataMap.getTraderIdNameMap().get(3L));
        assertEquals("Growth", cacheDataMap.getBasketIdNameMap().get(4L));
    }
}
