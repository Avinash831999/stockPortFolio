package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.support.EntityTestBuilders;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockBasketMapTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        StockBasketMap empty = new StockBasketMap();
        assertNull(empty.getId());
        assertNull(empty.getBasket());
        assertNull(empty.getStock());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        StockBasketMap map = EntityTestBuilders.stockBasketMap(1L, 2L, 3L, createdAt, updatedAt);
        assertEquals(1L, map.getId());
        assertEquals(2L, map.getBasket().getId());
        assertEquals(3L, map.getStock().getId());
        assertEquals(createdAt, map.getCreatedAt());
        assertEquals(updatedAt, map.getUpdatedAt());

        StockBasketMap same = EntityTestBuilders.stockBasketMap(1L, 2L, 3L, createdAt, updatedAt);
        StockBasketMap different = EntityTestBuilders.stockBasketMap(9L, 2L, 3L, createdAt, updatedAt);
        assertEquals(map, same);
        assertNotEquals(map, different);
        assertEquals(map.hashCode(), same.hashCode());
        assertTrue(map.toString().contains("basket"));
    }
}
