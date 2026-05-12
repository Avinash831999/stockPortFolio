package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_trader_id", columnList = "trader_id"),
        @Index(name = "idx_orders_trader_stock", columnList = "trader_id, stock_id"),
        @Index(name = "idx_orders_trader_orderStatus", columnList = "trader_id, status")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "trader_id", nullable = false)
    private Trader trader;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;
    private Integer quantity;
    private Float rate;
    private Float total;
    private TradeSide side;
    private OrderStatus status;
    private Date createdAt;
    private Date updatedAt;

}
