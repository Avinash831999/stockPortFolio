package com.orderbooking.stockportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradersHoldingsDto {

    private Long traderId;
    private String traderName;
    private List<HoldingStock> holdings;
//    private Long basketId;
//    private List<String> baskets;

}
