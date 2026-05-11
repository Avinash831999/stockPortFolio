package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.SectorDto;
import com.orderbooking.stockportfolio.service.SectorsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectorControllerTest {

    @Mock
    private SectorsService sectorsService;
    private SectorController controller;

    @BeforeEach
    void setUp() {
        controller = new SectorController(sectorsService);
    }

    @Test
    void getAllSectors_returnsOk() {
        when(sectorsService.getAllSectors()).thenReturn(List.of(new SectorDto()));
        ResponseEntity<List<SectorDto>> response = controller.getAllSectors();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSectorDetails_returnsOk() {
        when(sectorsService.getSectorDetailsById(1L)).thenReturn(new SectorDto());
        ResponseEntity<SectorDto> response = controller.getSectorDetails(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addSector_returnsCreated() {
        SectorDto dto = new SectorDto();
        when(sectorsService.addSector(dto)).thenReturn(dto);
        ResponseEntity<SectorDto> response = controller.addSector(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void removeSector_returnsNoContent() {
        ResponseEntity<Void> response = controller.removeSector(1L);
        verify(sectorsService).removeSector(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateSector_returnsOk() {
        SectorDto dto = new SectorDto();
        when(sectorsService.updateSector(1L, dto)).thenReturn(dto);
        ResponseEntity<SectorDto> response = controller.updateSector(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
