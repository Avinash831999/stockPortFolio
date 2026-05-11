package com.orderbooking.stockportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockBasketMapDto {
    private Long id;
    @NotNull
    private Long basketId;
    private String basketName;
    private String stockName;
    @NotNull
    private Long stockId;
    private Date createdAt;
    private Date updatedAt;
}
