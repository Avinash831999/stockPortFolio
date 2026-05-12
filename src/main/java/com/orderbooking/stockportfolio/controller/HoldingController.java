package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.HoldingDto;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;
import com.orderbooking.stockportfolio.entity.Holding;
import com.orderbooking.stockportfolio.service.HoldingsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/holdings")
public class HoldingController {

    private final HoldingsService holdingsService;

    public HoldingController(HoldingsService holdingsService) {
        this.holdingsService = holdingsService;
    }

//    @PutMapping
//    public ResponseEntity<HoldingDto> updateHoldings(@Valid @RequestBody HoldingDto holding) {
//        return ResponseEntity.ok(this.holdingsService.updateHoldings(holding));
//    }

    @GetMapping
    public ResponseEntity<TradersHoldingsDto> getHoldingsByTraderIdAndStockId(
            @RequestParam Long traderId,
            @RequestParam(required = false) Long stockId) {

        if (stockId != null) {
            return ResponseEntity.ok(
                    this.holdingsService
                            .getHoldingsByTraderIdAndStockId(traderId, stockId)
            );
        }

        return ResponseEntity.ok(
                this.holdingsService.getHoldingsByTraderId(traderId)
        );
    }


}
