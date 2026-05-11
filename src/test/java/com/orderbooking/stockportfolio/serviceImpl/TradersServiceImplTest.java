package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.TraderDto;
import com.orderbooking.stockportfolio.entity.Trader;
import com.orderbooking.stockportfolio.enums.TraderStatus;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.repository.TraderRepository;
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
class TradersServiceImplTest {

    @Mock
    private TraderRepository traderRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private TradersServiceImpl service;
    private Map<Long, String> traderMap;

    @Test
    void addTrader_success() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        TraderDto input = new TraderDto(null, "A", "a@a.com", "ABCDE1234F", "ACTIVE", null, null);
        when(traderRepository.save(any(Trader.class)))
                .thenReturn(new Trader(1L, "A", "a@a.com", "ABCDE1234F", TraderStatus.ACTIVE, new Date(), new Date()));
        TraderDto result = service.addTrader(input);
        assertEquals(1L, result.getId());
        assertEquals("A", traderMap.get(1L));
    }

    @Test
    void updateTrader_success_withDifferentInputFields() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        traderMap.put(1L, "Old");
        Trader existing = new Trader(1L, "Old", "old@a.com", "ABCDE1234F", TraderStatus.ACTIVE, new Date(), new Date());
        when(traderRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(traderRepository.save(any(Trader.class))).thenAnswer(i -> i.getArgument(0));

        TraderDto update = new TraderDto(null, "New", "new@a.com", "AAAAA1111A", "INACTIVE", null, new Date());
        TraderDto result = service.updateTrader(1L, update);
        assertEquals("New", result.getName());
        assertEquals("INACTIVE", result.getTraderStatus());
        assertEquals("New", traderMap.get(1L));
    }

    @Test
    void updateTrader_notFound_throws() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        TraderDto update = new TraderDto(null, "New", "new@a.com", "AAAAA1111A", "INACTIVE", null, new Date());
        assertThrows(DataNotFoundException.class, () -> service.updateTrader(1L, update));
    }

    @Test
    void removeTrader_success() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        traderMap.put(1L, "A");
        service.removeTrader(1L);
        verify(traderRepository).deleteById(1L);
    }

    @Test
    void removeTrader_notFound_throws() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        assertThrows(DataNotFoundException.class, () -> service.removeTrader(1L));
    }

    @Test
    void getAllTraders_mapsList() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(traderRepository.findAll()).thenReturn(List.of(
                new Trader(1L, "A", "a@a.com", "ABCDE1234F", TraderStatus.ACTIVE, new Date(), new Date())
        ));
        assertEquals(1, service.getAllTraders().size());
    }

    @Test
    void getTraderDetailsById_success() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        traderMap.put(1L, "A");
        when(traderRepository.findById(1L))
                .thenReturn(Optional.of(new Trader(1L, "A", "a@a.com", "ABCDE1234F", TraderStatus.ACTIVE, new Date(), new Date())));
        assertEquals("A", service.getTraderDetailsById(1L).getName());
    }

    @Test
    void getTraderDetailsById_notFound_throws() {
        service = new TradersServiceImpl(traderRepository, cacheDataMap);
        traderMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getTraderIdNameMap()).thenReturn(traderMap);
        assertThrows(DataNotFoundException.class, () -> service.getTraderDetailsById(1L));
    }
}
