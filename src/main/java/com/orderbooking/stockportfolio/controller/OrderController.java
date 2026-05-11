package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.OrderDto;
import com.orderbooking.stockportfolio.dto.PlaceOrderDto;
import com.orderbooking.stockportfolio.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@Valid @RequestBody PlaceOrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.placeOrder(orderDto));
    }

    @GetMapping("/{orderId}/{traderId}")
    public ResponseEntity<OrderDto> getOrderDetails(@PathVariable Long orderId, @PathVariable Long traderId) {
        return ResponseEntity.ok(this.orderService.getOrderDetails(orderId, traderId));
    }

    @GetMapping("/byTraderId/{traderId}")
    public ResponseEntity<List<OrderDto>> getOrderDetailsOfTrader(@PathVariable Long traderId) {
        return ResponseEntity.ok(this.orderService.getOrderDetailsOfTrader(traderId));
    }

    @PutMapping("/{orderId}/fill/trader/{traderId}")
    public ResponseEntity<OrderDto> fillOrder(@PathVariable Long orderId, @PathVariable Long traderId) {
        return ResponseEntity.ok(this.orderService.fillOrder(orderId, traderId));
    }

    @PutMapping("/{orderId}/cancel/trader/{traderId}")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId, @PathVariable Long traderId) {
        return ResponseEntity.ok(this.orderService.cancelOrder(orderId, traderId));
    }
}
