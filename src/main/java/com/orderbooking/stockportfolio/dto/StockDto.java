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
public class StockDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Float price;
    @NotNull
    private Long sectorId;
    private String sectorName;
    private Date createdAt;
    private Date updatedAt;
}
