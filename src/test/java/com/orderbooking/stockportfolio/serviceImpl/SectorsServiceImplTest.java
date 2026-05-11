package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.SectorDto;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.SectorRepository;
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
class SectorsServiceImplTest {

    @Mock
    private SectorRepository sectorRepository;
    @Mock
    private CacheDataMap cacheDataMap;

    private SectorsServiceImpl service;
    private Map<Long, String> sectorMap;
    private Map<Long, String> basketMap;


    @Test
    void getAllSectors_mapsList() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(sectorRepository.findAll()).thenReturn(List.of(new Sector(1L, "Tech", new Date(), new Date())));
        assertEquals(1, service.getAllSectors().size());
    }

    @Test
    void getSectorDetailsById_success() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(sectorRepository.findById(1L)).thenReturn(Optional.of(new Sector(1L, "Tech", new Date(), new Date())));
        assertEquals("Tech", service.getSectorDetailsById(1L).getName());
    }

    @Test
    void getSectorDetailsById_notFound_throws() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
//        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        when(sectorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> service.getSectorDetailsById(1L));
    }

    @Test
    void addSector_success() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        SectorDto dto = new SectorDto(null, "Tech", null, null);
        when(sectorRepository.save(any(Sector.class))).thenReturn(new Sector(1L, "Tech", new Date(), new Date()));
        SectorDto result = service.addSector(dto);
        assertEquals(1L, result.getId());
        assertEquals("Tech", sectorMap.get(1L));
    }

    @Test
    void addSector_duplicate_throws() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        sectorMap.put(1L, "Tech");
        assertThrows(DuplicateDataException.class, () -> service.addSector(new SectorDto(null, "Tech", null, null)));
    }

    @Test
    void removeSector_success() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        sectorMap.put(1L, "Tech");
        basketMap.put(1L, "B1");
        service.removeSector(1L);
        verify(sectorRepository).deleteById(1L);
        assertEquals(false, basketMap.containsKey(1L));
    }

    @Test
    void removeSector_notFound_throws() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        assertThrows(DataNotFoundException.class, () -> service.removeSector(1L));
    }

    @Test
    void updateSector_success() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        sectorMap.put(1L, "Tech");
        when(sectorRepository.findById(1L)).thenReturn(Optional.of(new Sector(1L, "Tech", new Date(), new Date())));
        when(sectorRepository.save(any(Sector.class))).thenAnswer(i -> i.getArgument(0));
        SectorDto result = service.updateSector(1L, new SectorDto(null, "Tech", null, null));
        assertEquals("Tech", result.getName());
    }

    @Test
    void updateSector_notFound_throws() {
        service = new SectorsServiceImpl(sectorRepository, cacheDataMap);
        sectorMap = new ConcurrentHashMap<>();
        basketMap = new ConcurrentHashMap<>();
        when(cacheDataMap.getSectorIdNameMap()).thenReturn(sectorMap);
//        when(cacheDataMap.getBasketIdNameMap()).thenReturn(basketMap);
        assertThrows(DataNotFoundException.class, () -> service.updateSector(1L, new SectorDto(null, "Missing", null, null)));
    }
}
