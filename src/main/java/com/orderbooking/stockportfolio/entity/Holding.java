package com.orderbooking.stockportfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(
    uniqueConstraints = {@UniqueConstraint(columnNames = {"traderId", "stockId"})},
    indexes = {
        @Index(name = "idx_holding_trader_id", columnList = "traderId"),
        @Index(name = "idx_holding_trader_stock", columnList = "traderId, stockId")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long traderId;
    private Long stockId;
    private Long sectorId;
    private Integer quantity;
    private Date updatedAt;
    
}
