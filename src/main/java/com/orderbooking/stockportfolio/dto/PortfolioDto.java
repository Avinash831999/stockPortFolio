package com.orderbooking.stockportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioDto {
    private Long traderId;
    private Map<String, Integer> positions;
    private Map<String, Integer> sectorBreakDown;
}
