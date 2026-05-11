package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.HoldingDto;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;
import com.orderbooking.stockportfolio.service.HoldingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HoldingControllerTest {

    @Mock
    private HoldingsService holdingsService;
    private HoldingController controller;

    @BeforeEach
    void setUp() {
        controller = new HoldingController(holdingsService);
    }

    @Test
    void updateHoldings_returnsOk() {
        HoldingDto dto = new HoldingDto();
        when(holdingsService.updateHoldings(dto)).thenReturn(dto);
        ResponseEntity<HoldingDto> response = controller.updateHoldings(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHoldingsByTraderIdAndStockId_returnsOk() {
        when(holdingsService.getHoldingsByTraderIdAndStockId(1L, 2L)).thenReturn(new TradersHoldingsDto());
        ResponseEntity<TradersHoldingsDto> response = controller.getHoldingsByTraderIdAndStockId(1L, 2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHoldingsByTraderId_returnsOk() {
        when(holdingsService.getHoldingsByTraderId(1L)).thenReturn(new TradersHoldingsDto());
        ResponseEntity<TradersHoldingsDto> response = controller.getHoldingsByTraderId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
