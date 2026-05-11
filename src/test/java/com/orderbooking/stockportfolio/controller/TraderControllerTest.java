package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.TraderDto;
import com.orderbooking.stockportfolio.service.TradersService;
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
class TraderControllerTest {

    @Mock
    private TradersService tradersService;
    private TraderController controller;

    @BeforeEach
    void setUp() {
        controller = new TraderController(tradersService);
    }

    @Test
    void getAllTraders_returnsOk() {
        when(tradersService.getAllTraders()).thenReturn(List.of(new TraderDto()));
        ResponseEntity<List<TraderDto>> response = controller.getAllTraders();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTraderDetails_returnsOk() {
        when(tradersService.getTraderDetailsById(1L)).thenReturn(new TraderDto());
        ResponseEntity<TraderDto> response = controller.getTraderDetails(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateTraderDetails_returnsOk() {
        TraderDto dto = new TraderDto();
        when(tradersService.updateTrader(1L, dto)).thenReturn(dto);
        ResponseEntity<TraderDto> response = controller.updateTraderDetails(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addTrader_returnsCreated() {
        TraderDto dto = new TraderDto();
        when(tradersService.addTrader(dto)).thenReturn(dto);
        ResponseEntity<TraderDto> response = controller.addTrader(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deleteTrader_returnsNoContent() {
        ResponseEntity<Void> response = controller.deleteTrader(1L);
        verify(tradersService).removeTrader(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
