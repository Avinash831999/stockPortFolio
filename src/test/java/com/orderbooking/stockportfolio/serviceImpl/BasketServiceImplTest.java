package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.BasketDto;
import com.orderbooking.stockportfolio.entity.Basket;
import com.orderbooking.stockportfolio.enums.BasketStatus;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.BasketRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceImplTest {

    @Mock
    private BasketRepository basketRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private BasketServiceImpl service;
    private Map<Long, String> basketMap;



    @Test
    void createBasket_success() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        BasketDto dto = new BasketDto(null, "Tech", "ACTIVE", null, null);
        Basket saved = new Basket(1L, "Tech", BasketStatus.ACTIVE, new Date(), new Date());
        when(basketRepository.save(any(Basket.class))).thenReturn(saved);

        BasketDto result = service.createBasket(dto);

        assertEquals(1L, result.getId());
        assertEquals("Tech", basketMap.get(1L));
        assertEquals("ACTIVE", result.getBasketStatus());
    }

    @Test
    void createBasket_duplicate_throws() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        basketMap.put(10L, "Tech");
        BasketDto dto = new BasketDto(null, "Tech", "ACTIVE", null, null);

        assertThrows(DuplicateDataException.class, () -> service.createBasket(dto));
    }

    @Test
    void delete_success() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        basketMap.put(1L, "Tech");
        service.delete(1L);
        verify(basketRepository).deleteById(1L);
        assertEquals(false, basketMap.containsKey(1L));
    }

    @Test
    void delete_missing_throws() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        assertThrows(DataNotFoundException.class, () -> service.delete(77L));
    }

    @Test
    void updateBasket_success() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        basketMap.put(1L, "Tech");
        Basket existing = new Basket(1L, "Tech", BasketStatus.ACTIVE, new Date(), new Date());
        when(basketRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(basketRepository.save(any(Basket.class))).thenAnswer(i -> i.getArgument(0));

        BasketDto input = new BasketDto(null, "Tech", "INACTIVE", null, null);
        BasketDto result = service.updateBasket(1L, input);

        assertEquals("INACTIVE", result.getBasketStatus());
        assertEquals("Tech", result.getName());
    }

    @Test
    void updateBasket_notFound_throws() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        BasketDto input = new BasketDto(null, "Unknown", "ACTIVE", null, null);
        assertThrows(DataNotFoundException.class, () -> service.updateBasket(9L, input));
    }

    @Test
    void getBasketList_mapsAll() {
        service = new BasketServiceImpl(basketRepository, cacheDataMap);
        basketMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        Basket b1 = new Basket(1L, "A", BasketStatus.ACTIVE, new Date(), new Date());
        Basket b2 = new Basket(2L, "B", BasketStatus.INACTIVE, new Date(), new Date());
        when(basketRepository.findAll()).thenReturn(List.of(b1, b2));

        List<BasketDto> result = service.getBasketList();
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
    }
}
