package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
        List<Order> findByTraderId(Long traderId);

}
