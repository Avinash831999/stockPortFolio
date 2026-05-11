package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.StockDto;
import com.orderbooking.stockportfolio.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/")
    public ResponseEntity<List<StockDto>> getAllStocks() {
        return ResponseEntity.ok(this.stockService.getAllStocks());
    }

    @GetMapping("/detailsById/{id}")
    public ResponseEntity<StockDto> getStockDetails(@PathVariable("id") Long stockId) {
        return ResponseEntity.ok(this.stockService.getStockDetailsById(stockId));
    }

    @GetMapping("/detailsByName/{name}")
    public ResponseEntity<StockDto> getStockDetailsByName(@PathVariable("name") String stockName) {
        return ResponseEntity.ok(this.stockService.getStockDetailsByName(stockName));
    }

    @PostMapping
    public ResponseEntity<StockDto> addStock(@Valid @RequestBody StockDto stockDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.stockService.addStock(stockDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockDto> updateStock(@Valid @PathVariable("id") Long stockId , @RequestBody StockDto stockDto) {
        return ResponseEntity.ok(this.stockService.updateStock(stockId, stockDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStock(@PathVariable("id") Long stockId) {
        this.stockService.removeStock(stockId);
        return ResponseEntity.noContent().build();
    }
}
