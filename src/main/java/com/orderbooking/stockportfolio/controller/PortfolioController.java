package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.PortfolioDto;
import com.orderbooking.stockportfolio.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/trader/{id}")
    public ResponseEntity<PortfolioDto> getTraderPortfolio(@PathVariable("id") Long traderId) {
        return ResponseEntity.ok(this.portfolioService.getTraderPortFolio(traderId));
    }
}
