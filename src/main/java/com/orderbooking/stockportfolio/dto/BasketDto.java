package com.orderbooking.stockportfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orderbooking.stockportfolio.enums.BasketStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @JsonProperty("basket_status")
    private String basketStatus;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
}
