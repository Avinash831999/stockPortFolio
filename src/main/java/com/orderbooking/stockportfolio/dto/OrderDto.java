package com.orderbooking.stockportfolio.dto;


import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private Long traderId;
    private String traderName;
    private Long stockId;
    private String stockName;
    private Long sectorId;
    private String sectorName;
    private Integer quantity;
    private Float rate;
    private Float total;
    private TradeSide side;
    private OrderStatus orderStatus;
    private Date createdAt;
    private Date updatedAt;
}
