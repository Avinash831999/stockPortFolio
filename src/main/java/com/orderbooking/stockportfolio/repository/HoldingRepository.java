package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding ,Long> {

   Optional<Holding> findByTraderIdAndStockId(Long traderId, Long stockId);
   boolean existsByTraderIdAndStockId(Long traderId, Long stockId);
   List<Holding> findByTraderId(Long traderId);

}
