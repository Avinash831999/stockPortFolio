package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.StockBasketMapBulk;
import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.service.BasketStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketStockControllerTest {

    @Mock
    private BasketStockService basketStockService;
    private BasketStockController controller;

    @BeforeEach
    void setUp() {
        controller = new BasketStockController(basketStockService);
    }

    @Test
    void createBasketStock_returnsCreated() {
        StockBasketMapDto dto = new StockBasketMapDto();
        when(basketStockService.createBasketStockMap(dto)).thenReturn(dto);
        ResponseEntity<StockBasketMapDto> response = controller.createBasketStock(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void bulkAddStockToBasket_returnsCreated() {
        StockBasketMapBulk bulk = new StockBasketMapBulk(Set.of(1L, 2L));
        when(basketStockService.bulkAddStocksToBasket(9L, bulk.getStockIds())).thenReturn(List.of(new StockBasketMapDto()));
        ResponseEntity<List<StockBasketMapDto>> response = controller.bulkAddStockToBasket(9L, bulk);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void bulkRemoveStocksFromBasket_setInput_throwsClassCastException() {
        StockBasketMapBulk bulk = new StockBasketMapBulk(Set.of(1L, 2L));
        assertThrows(ClassCastException.class, () -> controller.bulkRemoveStocksFromBasket(9L, bulk));
    }

    @Test
    void deleteBasketStock_returnsNoContent() {
        ResponseEntity<Void> response = controller.deleteBasketStock(9L, 3L);
        verify(basketStockService).delete(9L, 3L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
