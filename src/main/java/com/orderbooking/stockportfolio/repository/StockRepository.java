package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Long> {

    boolean existsByName(String name);
    Optional<Stock> findByName(String name);
    List<Stock> findAllByIdIn(List<Long> stockIds);
}
