package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.HoldingDto;
import com.orderbooking.stockportfolio.dto.TradersHoldingsDto;

import java.util.List;


public interface HoldingsService {



    HoldingDto addToTraderHoldings(HoldingDto holdingDto);
    HoldingDto updateHoldings(HoldingDto holdingDto);
    TradersHoldingsDto getHoldingsByTraderIdAndStockId(Long traderId, Long stockId);
    TradersHoldingsDto getHoldingsByTraderId(Long traderId);
    void deleteHolding(HoldingDto holdingDto);
//    HoldingDto addHolding(HoldingDto holdingDto);

}
