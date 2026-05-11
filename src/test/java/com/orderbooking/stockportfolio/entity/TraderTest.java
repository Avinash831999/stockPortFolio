package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.enums.TraderStatus;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraderTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        Trader empty = new Trader();
        assertNull(empty.getId());
        assertNull(empty.getName());
        assertNull(empty.getEmail());
        assertNull(empty.getPanNumber());
        assertNull(empty.getTraderStatus());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        Trader trader = new Trader(1L, "Alice", "alice@example.com", "ABCDE1234F",
                TraderStatus.ACTIVE, createdAt, updatedAt);
        assertEquals(1L, trader.getId());
        assertEquals("Alice", trader.getName());
        assertEquals("alice@example.com", trader.getEmail());
        assertEquals("ABCDE1234F", trader.getPanNumber());
        assertEquals(TraderStatus.ACTIVE, trader.getTraderStatus());
        assertEquals(createdAt, trader.getCreatedAt());
        assertEquals(updatedAt, trader.getUpdatedAt());

        Trader same = new Trader(1L, "Alice", "alice@example.com", "ABCDE1234F",
                TraderStatus.ACTIVE, createdAt, updatedAt);
        assertEquals(trader, same);
        assertEquals(trader.hashCode(), same.hashCode());
        assertTrue(trader.toString().contains("Alice"));
    }
}
