package com.orderbooking.stockportfolio.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SectorTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        Sector empty = new Sector();
        assertNull(empty.getId());
        assertNull(empty.getName());
        assertNull(empty.getStocks());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        Sector sector = new Sector(1L, "Technology", null, createdAt, updatedAt);
        assertEquals(1L, sector.getId());
        assertEquals("Technology", sector.getName());
        assertEquals(createdAt, sector.getCreatedAt());
        assertEquals(updatedAt, sector.getUpdatedAt());

        Sector same = new Sector(1L, "Technology", null, createdAt, updatedAt);
        assertEquals(sector, same);
        assertEquals(sector.hashCode(), same.hashCode());
        assertTrue(sector.toString().contains("Technology"));
    }
}
