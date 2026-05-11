package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.StockDto;
import com.orderbooking.stockportfolio.service.StockService;
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
class StockControllerTest {

    @Mock
    private StockService stockService;
    private StockController controller;

    @BeforeEach
    void setUp() {
        controller = new StockController(stockService);
    }

    @Test
    void getAllStocks_returnsOk() {
        when(stockService.getAllStocks()).thenReturn(List.of(new StockDto()));
        ResponseEntity<List<StockDto>> response = controller.getAllStocks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getStockDetails_returnsOk() {
        when(stockService.getStockDetailsById(1L)).thenReturn(new StockDto());
        ResponseEntity<StockDto> response = controller.getStockDetails(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getStockDetailsByName_returnsOk() {
        when(stockService.getStockDetailsByName("ABC")).thenReturn(new StockDto());
        ResponseEntity<StockDto> response = controller.getStockDetailsByName("ABC");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void addStock_returnsCreated() throws Exception {
        StockDto dto = new StockDto();
        when(stockService.addStock(dto)).thenReturn(dto);
        ResponseEntity<StockDto> response = controller.addStock(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void updateStock_returnsOk() {
        StockDto dto = new StockDto();
        when(stockService.updateStock(1L, dto)).thenReturn(dto);
        ResponseEntity<StockDto> response = controller.updateStock(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removeStock_returnsNoContent() {
        ResponseEntity<Void> response = controller.removeStock(1L);
        verify(stockService).removeStock(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
