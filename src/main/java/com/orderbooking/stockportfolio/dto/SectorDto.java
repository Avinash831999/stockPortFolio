package com.orderbooking.stockportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorDto {

    private Long id;
    @NotBlank
    private String name;
    private Date createdAt;
    private Date updatedAt;
}
