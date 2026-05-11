package com.orderbooking.stockportfolio.entity;

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
        assertNull(empty.getSectorId());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        Stock stock = new Stock(1L, "ABC", 123.4f, 10L, createdAt, updatedAt);
        assertEquals(1L, stock.getId());
        assertEquals("ABC", stock.getName());
        assertEquals(123.4f, stock.getPrice());
        assertEquals(10L, stock.getSectorId());
        assertEquals(createdAt, stock.getCreatedAt());
        assertEquals(updatedAt, stock.getUpdatedAt());

        Stock same = new Stock(1L, "ABC", 123.4f, 10L, createdAt, updatedAt);
        assertEquals(stock, same);
        assertEquals(stock.hashCode(), same.hashCode());
        assertTrue(stock.toString().contains("ABC"));
    }
}
