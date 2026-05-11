package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Trader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraderRepository extends JpaRepository<Trader,Long> {
}
