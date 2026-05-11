package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.TraderDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TradersService {

    TraderDto addTrader(TraderDto traderDto);
    TraderDto updateTrader(Long traderId, TraderDto traderDto);
    void removeTrader(Long traderId);
    List<TraderDto> getAllTraders();
    TraderDto getTraderDetailsById(Long traderId);

}
