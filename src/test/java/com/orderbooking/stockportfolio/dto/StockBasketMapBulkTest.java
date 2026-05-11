package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.HashSet;

public class StockBasketMapBulkTest {

    @Test
    public void testNoArgsConstructor() {
        StockBasketMapBulk dto = new StockBasketMapBulk();
        assertNull(dto.getStockIds());
    }

    @Test
    public void testAllArgsConstructor() {
        Set<Long> stockIds = new HashSet<>();
        stockIds.add(1L);
        stockIds.add(2L);
        StockBasketMapBulk dto = new StockBasketMapBulk(stockIds);
        assertEquals(stockIds, dto.getStockIds());
    }

    @Test
    public void testGettersAndSetters() {
        StockBasketMapBulk dto = new StockBasketMapBulk();
        Set<Long> stockIds = new HashSet<>();
        stockIds.add(1L);
        stockIds.add(2L);
        dto.setStockIds(stockIds);
        assertEquals(stockIds, dto.getStockIds());
    }

    @Test
    public void testEqualsAndHashCode() {
        Set<Long> stockIds = new HashSet<>();
        stockIds.add(1L);
        StockBasketMapBulk dto1 = new StockBasketMapBulk(stockIds);
        StockBasketMapBulk dto2 = new StockBasketMapBulk(stockIds);
        StockBasketMapBulk dto3 = new StockBasketMapBulk(new HashSet<>());
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        Set<Long> stockIds = new HashSet<>();
        stockIds.add(1L);
        StockBasketMapBulk dto = new StockBasketMapBulk(stockIds);
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("1"));
    }
}
