package com.orderbooking.stockportfolio.dto;

import com.orderbooking.stockportfolio.enums.RiskFactor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverlapsInfo {

    List<Overlap> overlaps;
    private String dominantBasket;
    private String riskFlag;
}
