package com.orderbooking.stockportfolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_stockbasketmap_basket_id", columnList = "basket_id"),
                @Index(name = "idx_stockbasketmap_stock_id", columnList = "stock_id"),
                @Index(name = "idx_stockbasketmap_basket_stock", columnList = "basket_id, stock_id")
        }
)
public class StockBasketMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", nullable = false)
    private Basket basket;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    private Date createdAt;
    private Date updatedAt;

}
