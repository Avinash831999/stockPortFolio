package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OverlapTest {

    @Test
    public void testNoArgsConstructor() {
        Overlap dto = new Overlap();
        assertNull(dto.getBasket());
        assertNull(dto.getOverlap());
    }

    @Test
    public void testAllArgsConstructor() {
        Overlap dto = new Overlap("Basket1", 0.5f);
        assertEquals("Basket1", dto.getBasket());
        assertEquals(0.5f, dto.getOverlap());
    }

    @Test
    public void testGettersAndSetters() {
        Overlap dto = new Overlap();
        dto.setBasket("Basket1");
        dto.setOverlap(0.5f);
        assertEquals("Basket1", dto.getBasket());
        assertEquals(0.5f, dto.getOverlap());
    }

    @Test
    public void testEqualsAndHashCode() {
        Overlap dto1 = new Overlap("B", 0.5f);
        Overlap dto2 = new Overlap("B", 0.5f);
        Overlap dto3 = new Overlap("C", 0.5f);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        Overlap dto = new Overlap("B", 0.5f);
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("B"));
    }
}
