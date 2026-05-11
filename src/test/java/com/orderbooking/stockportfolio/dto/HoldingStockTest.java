package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Date;

public class HoldingStockTest {

    @Test
    public void testNoArgsConstructor() {
        HoldingStock dto = new HoldingStock();
        assertNull(dto.getId());
        assertNull(dto.getStockId());
        assertNull(dto.getStockName());
        assertNull(dto.getSectorId());
        assertNull(dto.getSectorName());
        assertNull(dto.getQuantity());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date updated = new Date();
        HoldingStock dto = new HoldingStock(1L, 2L, "Stock", 3L, "Sector", 100, updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getStockId());
        assertEquals("Stock", dto.getStockName());
        assertEquals(3L, dto.getSectorId());
        assertEquals("Sector", dto.getSectorName());
        assertEquals(100, dto.getQuantity());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        HoldingStock dto = new HoldingStock();
        Date updated = new Date();
        dto.setId(1L);
        dto.setStockId(2L);
        dto.setStockName("Stock");
        dto.setSectorId(3L);
        dto.setSectorName("Sector");
        dto.setQuantity(100);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getStockId());
        assertEquals("Stock", dto.getStockName());
        assertEquals(3L, dto.getSectorId());
        assertEquals("Sector", dto.getSectorName());
        assertEquals(100, dto.getQuantity());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date updated = new Date();
        HoldingStock dto1 = new HoldingStock(1L, 2L, "S", 3L, "Sec", 100, updated);
        HoldingStock dto2 = new HoldingStock(1L, 2L, "S", 3L, "Sec", 100, updated);
        HoldingStock dto3 = new HoldingStock(2L, 2L, "S", 3L, "Sec", 100, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        HoldingStock dto = new HoldingStock(1L, 2L, "S", 3L, "Sec", 100, new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("S"));
    }
}
