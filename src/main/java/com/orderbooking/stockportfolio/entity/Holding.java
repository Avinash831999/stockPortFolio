package com.orderbooking.stockportfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(
    uniqueConstraints = {@UniqueConstraint(name = "uk_holding_trader_stock", columnNames = {"trader_id", "stock_id"})},
    indexes = {
        @Index(name = "idx_holding_trader_id", columnList = "trader_id"),
        @Index(name = "idx_holding_trader_stock", columnList = "trader_id, stock_id")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    private Integer quantity;
    private Date updatedAt;

}
