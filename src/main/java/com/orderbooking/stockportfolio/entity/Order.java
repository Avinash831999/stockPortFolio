package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "orders",indexes = {
        @Index(name = "idx_orders_traderId", columnList = "traderId"),
        @Index(name = "idx_orders_traderId_stockId", columnList = "traderId, stockId")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long traderId;
    private Long stockId;
    private Long sectorId;
    private Integer quantity;
    private Float rate;
    private Float total;
    private TradeSide side;
    private OrderStatus status;
    private Date createdAt;
    private Date updatedAt;

}
