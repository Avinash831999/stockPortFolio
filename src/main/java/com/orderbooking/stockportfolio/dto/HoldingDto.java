package com.orderbooking.stockportfolio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoldingDto {
    private Long id;
    @NotNull
    private Long traderId;
    @NotNull
    private Long stockId;
    @NotNull
    private Long sectorId;
    @NotNull
    private Integer quantity;
    private Long basketId;
    private Date updatedAt;
}
