package com.orderbooking.stockportfolio.repository;

import com.orderbooking.stockportfolio.entity.Order;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

        List<Order> findByTrader_IdAndStatus(Long traderId, OrderStatus status);

        List<Order> findByTrader_Id(Long traderId);

}
