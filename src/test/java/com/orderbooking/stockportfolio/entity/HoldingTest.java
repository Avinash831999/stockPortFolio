package com.orderbooking.stockportfolio.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HoldingTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        Holding empty = new Holding();
        assertNull(empty.getId());
        assertNull(empty.getTraderId());
        assertNull(empty.getStockId());
        assertNull(empty.getSectorId());
        assertNull(empty.getQuantity());
        assertNull(empty.getUpdatedAt());

        Date updatedAt = new Date();
        Holding holding = new Holding(1L, 2L, 3L, 4L, 50, updatedAt);
        assertEquals(1L, holding.getId());
        assertEquals(2L, holding.getTraderId());
        assertEquals(3L, holding.getStockId());
        assertEquals(4L, holding.getSectorId());
        assertEquals(50, holding.getQuantity());
        assertEquals(updatedAt, holding.getUpdatedAt());

        Holding same = new Holding(1L, 2L, 3L, 4L, 50, updatedAt);
        Holding different = new Holding(9L, 2L, 3L, 4L, 50, updatedAt);
        assertEquals(holding, same);
        assertNotEquals(holding, different);
        assertEquals(holding.hashCode(), same.hashCode());
        assertTrue(holding.toString().contains("quantity=50"));
    }
}
