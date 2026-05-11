package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.StockBasketMapBulk;
import com.orderbooking.stockportfolio.dto.StockBasketMapDto;
import com.orderbooking.stockportfolio.service.BasketStockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/basket-stocks")
public class BasketStockController {

    private final BasketStockService basketStockService;

    public BasketStockController(BasketStockService basketStockService) {
        this.basketStockService = basketStockService;
    }

    @PostMapping
    public ResponseEntity<StockBasketMapDto> createBasketStock(@Valid @RequestBody StockBasketMapDto stockBasketMap) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.basketStockService.createBasketStockMap(stockBasketMap));
    }


    @PostMapping("/bulkAddStockToBasket/{basketId}")
    public ResponseEntity<List<StockBasketMapDto>> bulkAddStockToBasket(@PathVariable Long basketId, @RequestBody StockBasketMapBulk stockIds) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.basketStockService.bulkAddStocksToBasket(basketId, stockIds.getStockIds()));
    }

    @PostMapping("/bulkRemoveStockFromBasket/{basketId}")
    public ResponseEntity<Void> bulkRemoveStocksFromBasket(@PathVariable Long basketId, @RequestBody StockBasketMapBulk stockIds) {
        this.basketStockService.bulkRemoveStocksFromBasket(basketId, (List<Long>) stockIds.getStockIds());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{basketId}/{stockId}")
    public ResponseEntity<Void> deleteBasketStock(@PathVariable Long basketId, @PathVariable Long stockId) {
        this.basketStockService.delete(basketId,stockId);
        return ResponseEntity.noContent().build();
    }
}
