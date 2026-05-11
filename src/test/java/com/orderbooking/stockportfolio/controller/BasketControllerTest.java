package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.BasketDto;
import com.orderbooking.stockportfolio.service.BasketService;
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
class BasketControllerTest {

    @Mock
    private BasketService basketService;
    private BasketController controller;

    @BeforeEach
    void setUp() {
        controller = new BasketController(basketService);
    }

    @Test
    void getBasketList_returnsOk() {
        when(basketService.getBasketList()).thenReturn(List.of(new BasketDto()));
        ResponseEntity<List<BasketDto>> response = controller.getBasketList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void createBasket_returnsCreated() {
        BasketDto dto = new BasketDto();
        when(basketService.createBasket(dto)).thenReturn(dto);
        ResponseEntity<BasketDto> response = controller.createBasket(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateBasket_returnsOk() {
        BasketDto dto = new BasketDto();
        when(basketService.updateBasket(1L, dto)).thenReturn(dto);
        ResponseEntity<BasketDto> response = controller.updateBasket(1L, dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteBasket_returnsNoContent() {
        ResponseEntity<Void> response = controller.deleteBasket(1L);
        verify(basketService).delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
