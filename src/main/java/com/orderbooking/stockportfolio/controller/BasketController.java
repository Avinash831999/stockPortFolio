package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.BasketDto;
import com.orderbooking.stockportfolio.service.BasketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/baskets")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public ResponseEntity<List<BasketDto>> getBasketList(){
        return ResponseEntity.ok(this.basketService.getBasketList());
    }

    @PostMapping
    public ResponseEntity<BasketDto> createBasket(@Valid @RequestBody BasketDto basketDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.basketService.createBasket(basketDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasketDto> updateBasket(@Valid @PathVariable("id") Long basketId,  @RequestBody BasketDto basketDto) {
        return ResponseEntity.ok(this.basketService.updateBasket(basketId, basketDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable("id") Long stockId) {
        this.basketService.delete(stockId);
        return ResponseEntity.noContent().build();
    }
}
