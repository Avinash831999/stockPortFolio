package com.orderbooking.stockportfolio.dto;

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
    private String basketStatus;
    private Date createdAt;
    private Date updatedAt;
}
