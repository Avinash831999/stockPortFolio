package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.StockDto;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private StockServiceImpl service;
    private Map<Long, String> stockMap;
    private Map<Long, String> sectorMap;


    @Test
    void addStock_success() throws Exception {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(stockRepository.save(any(Stock.class))).thenReturn(new Stock(1L, "ABC", 100f, 10L, new Date(), new Date()));
        StockDto result = service.addStock(new StockDto(null, "ABC", 100f, 10L, null, null, null));
        assertEquals(1L, result.getId());
        assertEquals("ABC", stockMap.get(1L));
    }

    @Test
    void addStock_duplicate_throws() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        stockMap.put(1L, "ABC");
        assertThrows(DuplicateDataException.class, () -> service.addStock(new StockDto(null, "ABC", 100f, 10L, null, null, null)));
    }

    @Test
    void updateStock_success() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        stockMap.put(1L, "ABC");
        when(stockRepository.findById(1L)).thenReturn(Optional.of(new Stock(1L, "ABC", 100f, 10L, new Date(), new Date())));
        when(stockRepository.save(any(Stock.class))).thenAnswer(i -> i.getArgument(0));
        StockDto result = service.updateStock(1L, new StockDto(null, "ABC", 200f, 10L, null, null, null));
        assertEquals(200f, result.getPrice());
    }

    @Test
    void updateStock_notFound_throws() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        assertThrows(DataNotFoundException.class, () -> service.updateStock(1L, new StockDto(null, "X", 100f, 10L, null, null, null)));
    }

    @Test
    void removeStock_success() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        stockMap.put(1L, "ABC");
        service.removeStock(1L);
        verify(stockRepository).deleteById(1L);
        assertEquals(false, stockMap.containsKey(1L));
    }

    @Test
    void removeStock_notFound_throws() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        assertThrows(DataNotFoundException.class, () -> service.removeStock(1L));
    }

    @Test
    void getAllStocks_mapsList() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(stockRepository.findAll()).thenReturn(List.of(new Stock(1L, "ABC", 100f, 10L, new Date(), new Date())));
        assertEquals(1, service.getAllStocks().size());
    }

    @Test
    void getStockDetailsById_success() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(stockRepository.findById(1L)).thenReturn(Optional.of(new Stock(1L, "ABC", 100f, 10L, new Date(), new Date())));
        assertEquals("ABC", service.getStockDetailsById(1L).getName());
    }

    @Test
    void getStockDetailsById_notFound_throws() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.getStockDetailsById(1L));
    }

    @Test
    void getStockDetailsByName_success() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(stockRepository.findByName("ABC")).thenReturn(Optional.of(new Stock(1L, "ABC", 100f, 10L, new Date(), new Date())));
        assertEquals("ABC", service.getStockDetailsByName("ABC").getName());
    }

    @Test
    void getStockDetailsByName_notFound_throws() {
        service = new StockServiceImpl(stockRepository, cacheDataMap);
        stockMap = new ConcurrentHashMap<>();
        sectorMap = new ConcurrentHashMap<>();
        sectorMap.put(10L, "Tech");
//        when(cacheDataMap.getStockIdNameMap()).thenReturn(stockMap);
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(stockRepository.findByName("ABC")).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.getStockDetailsByName("ABC"));
    }
}
