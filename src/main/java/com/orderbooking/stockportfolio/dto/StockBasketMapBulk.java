package com.orderbooking.stockportfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockBasketMapBulk {
    @JsonProperty("stock_ids")
    Set<Long> stockIds;
}
