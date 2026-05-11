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
                @Index(name = "idx_stockbasketmap_basketId", columnList = "basketId"),
                @Index(name = "idx_stockbasketmap_stockId", columnList = "stockId"),
                @Index(name = "idx_stockbasketmap_basket_stock", columnList = "basketId, stockId")
        }
)
public class StockBasketMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long basketId;
    private Long stockId;
    private Date createdAt;
    private Date updatedAt;

}
