package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByName(String name);
}
