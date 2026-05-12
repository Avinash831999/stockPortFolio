package com.orderbooking.stockportfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("sector_id")
    private Long sectorId;
    @JsonProperty("sector_name")
    private String sectorName;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
