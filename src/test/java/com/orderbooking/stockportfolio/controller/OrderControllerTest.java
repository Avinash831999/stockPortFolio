package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.OrderDto;
import com.orderbooking.stockportfolio.dto.PlaceOrderDto;
import com.orderbooking.stockportfolio.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        controller = new OrderController(orderService);
    }

    @Test
    void placeOrder_returnsCreated() {
        PlaceOrderDto dto = new PlaceOrderDto();
        when(orderService.placeOrder(dto)).thenReturn(new OrderDto());
        ResponseEntity<OrderDto> response = controller.placeOrder(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getOrderDetails_returnsOk() {
        when(orderService.getOrderDetails(1L, 2L)).thenReturn(new OrderDto());
        ResponseEntity<OrderDto> response = controller.getOrderDetails(1L, 2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getOrderDetailsOfTrader_returnsOk() {
        when(orderService.getOrderDetailsOfTrader(1L)).thenReturn(List.of(new OrderDto()));
        ResponseEntity<List<OrderDto>> response = controller.getOrderDetailsOfTrader(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void fillOrder_returnsOk() {
        when(orderService.fillOrder(1L, 2L)).thenReturn(new OrderDto());
        ResponseEntity<OrderDto> response = controller.fillOrder(1L, 2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void cancelOrder_returnsOk() {
        when(orderService.cancelOrder(1L, 2L)).thenReturn(new OrderDto());
        ResponseEntity<OrderDto> response = controller.cancelOrder(1L, 2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
