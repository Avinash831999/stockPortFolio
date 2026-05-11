package com.orderbooking.stockportfolio.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockBasketMapTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        StockBasketMap empty = new StockBasketMap();
        assertNull(empty.getId());
        assertNull(empty.getBasketId());
        assertNull(empty.getStockId());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        StockBasketMap map = new StockBasketMap(1L, 2L, 3L, createdAt, updatedAt);
        assertEquals(1L, map.getId());
        assertEquals(2L, map.getBasketId());
        assertEquals(3L, map.getStockId());
        assertEquals(createdAt, map.getCreatedAt());
        assertEquals(updatedAt, map.getUpdatedAt());

        StockBasketMap same = new StockBasketMap(1L, 2L, 3L, createdAt, updatedAt);
        assertEquals(map, same);
        assertEquals(map.hashCode(), same.hashCode());
        assertTrue(map.toString().contains("basketId=2"));
    }
}
