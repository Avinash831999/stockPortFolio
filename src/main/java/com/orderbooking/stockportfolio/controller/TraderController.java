package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.TraderDto;
import com.orderbooking.stockportfolio.service.TradersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traders")
@CrossOrigin
public class TraderController {

    private final TradersService tradersService;

    public TraderController(TradersService tradersService) {
        this.tradersService = tradersService;
    }

    @GetMapping
    public ResponseEntity<List<TraderDto>> getAllTraders(){
       return ResponseEntity.ok(this.tradersService.getAllTraders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TraderDto> getTraderDetails(@PathVariable("id") Long traderId){
       return ResponseEntity.ok(this.tradersService.getTraderDetailsById(traderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TraderDto> updateTraderDetails(@PathVariable("id") Long traderId, @Valid @RequestBody TraderDto traderDto){
        return ResponseEntity.ok(this.tradersService.updateTrader(traderId, traderDto));
    }

    @PostMapping
    public ResponseEntity<TraderDto> addTrader(@Valid @RequestBody TraderDto traderDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.tradersService.addTrader(traderDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrader(@PathVariable("id") Long traderId){
        this.tradersService.removeTrader(traderId);
        return ResponseEntity.noContent().build();
    }

}
