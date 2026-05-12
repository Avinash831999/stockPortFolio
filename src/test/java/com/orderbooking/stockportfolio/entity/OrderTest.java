package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import com.orderbooking.stockportfolio.support.EntityTestBuilders;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        Order empty = new Order();
        assertNull(empty.getId());
        assertNull(empty.getTrader());
        assertNull(empty.getStock());
        assertNull(empty.getSector());
        assertNull(empty.getQuantity());
        assertNull(empty.getRate());
        assertNull(empty.getTotal());
        assertNull(empty.getSide());
        assertNull(empty.getStatus());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        Order order = EntityTestBuilders.order(1L, 10L, 20L, 30L, 5, 100.5f, 502.5f,
                TradeSide.BUY, OrderStatus.PENDING, createdAt, updatedAt);
        assertEquals(1L, order.getId());
        assertEquals(10L, order.getTrader().getId());
        assertEquals(20L, order.getStock().getId());
        assertEquals(30L, order.getSector().getId());
        assertEquals(5, order.getQuantity());
        assertEquals(100.5f, order.getRate());
        assertEquals(502.5f, order.getTotal());
        assertEquals(TradeSide.BUY, order.getSide());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(updatedAt, order.getUpdatedAt());
        assertTrue(order.toString().contains("PENDING"));
    }
}
