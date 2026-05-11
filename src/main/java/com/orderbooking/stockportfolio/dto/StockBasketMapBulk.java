package com.orderbooking.stockportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockBasketMapBulk {
    Set<Long> stockIds;
}
