package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.support.EntityTestBuilders;
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
        assertNull(empty.getTrader());
        assertNull(empty.getStock());
        assertNull(empty.getSector());
        assertNull(empty.getBasket());
        assertNull(empty.getQuantity());
        assertNull(empty.getUpdatedAt());

        Date updatedAt = new Date();
        Holding holding = EntityTestBuilders.holding(1L, 2L, 3L, 4L, 50, updatedAt);
        assertEquals(1L, holding.getId());
        assertEquals(2L, holding.getTrader().getId());
        assertEquals(3L, holding.getStock().getId());
        assertEquals(4L, holding.getSector().getId());
        assertEquals(50, holding.getQuantity());
        assertEquals(updatedAt, holding.getUpdatedAt());

        Holding same = EntityTestBuilders.holding(1L, 2L, 3L, 4L, 50, updatedAt);
        Holding different = EntityTestBuilders.holding(9L, 2L, 3L, 4L, 50, updatedAt);
        assertEquals(holding, same);
        assertNotEquals(holding, different);
        assertEquals(holding.hashCode(), same.hashCode());
        assertTrue(holding.toString().contains("quantity=50"));
    }
}
