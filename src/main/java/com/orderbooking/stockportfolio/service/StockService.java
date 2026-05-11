package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.StockDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StockService {

    StockDto addStock(StockDto stockDto) throws Exception;
    StockDto updateStock(Long stockId, StockDto stockDto);
    void removeStock(Long stockId);
    List<StockDto> getAllStocks();
    StockDto getStockDetailsById(Long stockId);
    StockDto getStockDetailsByName(String name);
}
