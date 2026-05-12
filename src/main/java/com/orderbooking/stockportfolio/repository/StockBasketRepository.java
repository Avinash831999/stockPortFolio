package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.StockBasketMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StockBasketRepository extends JpaRepository<StockBasketMap, Long> {
    boolean existsByBasket_IdAndStock_Id(Long basketId, Long stockId);

    void deleteByBasket_IdAndStock_Id(Long basketId, Long stockId);

    List<StockBasketMap> findByBasket_Id(Long basketId);

    void deleteByBasket_IdAndStock_IdIn(Long basketId, Collection<Long> stockIds);
}
