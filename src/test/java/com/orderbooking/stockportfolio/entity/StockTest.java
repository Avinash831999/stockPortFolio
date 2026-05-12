package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.support.EntityTestBuilders;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        Stock empty = new Stock();
        assertNull(empty.getId());
        assertNull(empty.getName());
        assertNull(empty.getPrice());
        assertNull(empty.getSector());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        Stock stock = EntityTestBuilders.stock(1L, "ABC", 123.4f, 10L, createdAt, updatedAt);
        assertEquals(1L, stock.getId());
        assertEquals("ABC", stock.getName());
        assertEquals(123.4f, stock.getPrice());
        assertEquals(10L, stock.getSector().getId());
        assertEquals(createdAt, stock.getCreatedAt());
        assertEquals(updatedAt, stock.getUpdatedAt());

        Stock same = EntityTestBuilders.stock(1L, "ABC", 123.4f, 10L, createdAt, updatedAt);
        assertEquals(stock, same);
        assertEquals(stock.hashCode(), same.hashCode());
        assertTrue(stock.toString().contains("ABC"));
    }
}
