package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.StockBasketMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockBasketRepository extends JpaRepository<StockBasketMap, Long> {
    boolean existsByBasketIdAndStockId(Long basketId, Long stockId);
    void deleteByBasketIdAndStockId(Long basketId, Long stockId);
    List<StockBasketMap> findByBasketId(Long basketId);
    void deleteAllByStockIdIn(List<Long> stockId);
}
