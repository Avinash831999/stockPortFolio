package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.PortfolioDto;
import com.orderbooking.stockportfolio.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioControllerTest {

    @Mock
    private PortfolioService portfolioService;
    private PortfolioController controller;

    @BeforeEach
    void setUp() {
        controller = new PortfolioController(portfolioService);
    }

    @Test
    void getTraderPortfolio_returnsOk() {
        when(portfolioService.getTraderPortFolio(1L)).thenReturn(new PortfolioDto());
        ResponseEntity<PortfolioDto> response = controller.getTraderPortfolio(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
