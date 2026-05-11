package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.PortfolioDto;

public interface PortfolioService {
    PortfolioDto getTraderPortFolio(Long traderId);
}
