package com.orderbooking.stockportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Overlap {

    private String basket;
    private Float overlap;

}
