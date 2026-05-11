package com.orderbooking.stockportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingStock {
    private Long id;
    private Long stockId;
    private String stockName;
    private Long sectorId;
    private String sectorName;
    private Integer quantity;
    private Date updatedAt;
}
