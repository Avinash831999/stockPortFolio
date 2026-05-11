package com.orderbooking.stockportfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderbooking.stockportfolio.enums.TradeSide;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDto {

    @NotNull
    @JsonProperty("trader_id")
    private Long traderId;
    @NotNull
    @JsonProperty("stock_id")
    private Long stockId;
    @NotNull
    @JsonProperty("sector_id")
    private Long sectorId;
    @NotNull
    private Integer quantity;
    @NotBlank
    private String side;
}
