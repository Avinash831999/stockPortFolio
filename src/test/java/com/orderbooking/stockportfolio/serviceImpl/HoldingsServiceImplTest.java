package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.HoldingDto;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
import com.orderbooking.stockportfolio.repository.HoldingRepository;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.repository.TraderRepository;
import com.orderbooking.stockportfolio.support.EntityTestBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HoldingsServiceImplTest {

    @Mock
    private HoldingRepository holdingRepository;
    @Mock
    private SectorRepository sectorRepository;
    @Mock
    private TraderRepository traderRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private HoldingsServiceImpl service;

    private void stubRepositoriesForHoldingDto(HoldingDto dto) {
        when(traderRepository.findById(dto.getTraderId())).thenReturn(Optional.of(EntityTestBuilders.trader(dto.getTraderId())));
        when(stockRepository.findById(dto.getStockId())).thenReturn(Optional.of(EntityTestBuilders.stock(dto.getStockId(), dto.getSectorId())));
        when(sectorRepository.findById(dto.getSectorId())).thenReturn(Optional.of(EntityTestBuilders.sector(dto.getSectorId())));
        if (dto.getBasketId() != null) {
            when(basketRepository.findById(dto.getBasketId())).thenReturn(Optional.of(EntityTestBuilders.basket(dto.getBasketId())));
        }
    }

    @Test
    void updateHoldings_success() {
        service = new HoldingsServiceImpl(holdingRepository, sectorRepository, traderRepository, stockRepository, basketRepository, cacheDataMap);
        HoldingDto dto = new HoldingDto(1L, 1L, 2L, 3L, 50, 10L, new Date());
        stubRepositoriesForHoldingDto(dto);
        when(holdingRepository.existsById(1L)).thenReturn(true);
        when(holdingRepository.save(any(Holding.class))).thenAnswer(i -> i.getArgument(0));

        HoldingDto result = service.updateHoldings(dto);
        assertEquals(50, result.getQuantity());
    }

    @Test
    void updateHoldings_notFound_throws() {
        service = new HoldingsServiceImpl(holdingRepository, sectorRepository, traderRepository, stockRepository, basketRepository, cacheDataMap);
        HoldingDto dto = new HoldingDto(1L, 1L, 2L, 3L, 50, 10L, new Date());
        stubRepositoriesForHoldingDto(dto);
        when(holdingRepository.existsById(1L)).thenReturn(false);
        assertThrows(DataNotFoundException.class, () -> service.updateHoldings(dto));
    }

    @Test
    void addToTraderHoldings_success() {
        service = new HoldingsServiceImpl(holdingRepository, sectorRepository, traderRepository, stockRepository, basketRepository, cacheDataMap);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        traderMap.put(1L, "T1");
        stockMap.put(2L, "S2");
        HoldingDto dto = new HoldingDto(null, 1L, 2L, 3L, 30, 10L, new Date());
        stubRepositoriesForHoldingDto(dto);
        when(holdingRepository.existsByTrader_IdAndStock_Id(1L, 2L)).thenReturn(false);
        when(holdingRepository.save(any(Holding.class))).thenAnswer(i -> i.getArgument(0));

        HoldingDto result = service.addToTraderHoldings(dto);
        assertEquals(30, result.getQuantity());
    }

    @Test
    void addToTraderHoldings_missingStock_throws() {
        service = new HoldingsServiceImpl(holdingRepository, sectorRepository, traderRepository, stockRepository, basketRepository, cacheDataMap);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        traderMap.put(1L, "T1");
        HoldingDto dto = new HoldingDto(null, 1L, 2L, 3L, 30, 10L, new Date());
        assertThrows(DataNotFoundException.class, () -> service.addToTraderHoldings(dto));
    }

    @Test
    void getHoldingsByTraderIdAndStockId_mapsWhenPresent() {
        service = new HoldingsServiceImpl(holdingRepository, sectorRepository, traderRepository, stockRepository, basketRepository, cacheDataMap);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        traderMap.put(1L, "T1");
        stockMap.put(2L, "S2");
        sectorMap.put(3L, "Sec");
        Holding holding = EntityTestBuilders.holding(7L, 1L, 2L, 3L, 10, new Date());
        when(holdingRepository.findByTrader_IdAndStock_Id(1L, 2L)).thenReturn(Optional.of(holding));

        TradersHoldingsDto result = service.getHoldingsByTraderIdAndStockId(1L, 2L);
        assertEquals("T1", result.getTraderName());
        assertEquals(1, result.getHoldings().size());
    }

    @Test
    void getHoldingsByTraderId_mapsList() {
        service = new HoldingsServiceImpl(holdingRepository, sectorRepository, traderRepository, stockRepository, basketRepository, cacheDataMap);
        Map<Long, String> traderMap = new ConcurrentHashMap<>();
        Map<Long, String> stockMap = new ConcurrentHashMap<>();
        Map<Long, String> sectorMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        traderMap.put(1L, "T1");
        stockMap.put(2L, "S2");
        sectorMap.put(3L, "Sec");
        when(holdingRepository.findByTrader_Id(1L)).thenReturn(List.of(EntityTestBuilders.holding(7L, 1L, 2L, 3L, 10, new Date())));
        TradersHoldingsDto result = service.getHoldingsByTraderId(1L);
        assertEquals("S2", result.getHoldings().get(0).getStockName());
    }
}
