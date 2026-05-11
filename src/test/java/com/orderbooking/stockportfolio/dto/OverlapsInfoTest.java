package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

public class OverlapsInfoTest {

    @Test
    public void testNoArgsConstructor() {
        OverlapsInfo dto = new OverlapsInfo();
        assertNull(dto.getOverlaps());
        assertNull(dto.getDominantBasket());
        assertNull(dto.getRiskFlag());
    }

    @Test
    public void testAllArgsConstructor() {
        List<Overlap> overlaps = new ArrayList<>();
        overlaps.add(new Overlap("Basket1", 0.5f));
        OverlapsInfo dto = new OverlapsInfo(overlaps, "Dominant", "High");
        assertEquals(overlaps, dto.getOverlaps());
        assertEquals("Dominant", dto.getDominantBasket());
        assertEquals("High", dto.getRiskFlag());
    }

    @Test
    public void testGettersAndSetters() {
        OverlapsInfo dto = new OverlapsInfo();
        List<Overlap> overlaps = new ArrayList<>();
        overlaps.add(new Overlap("Basket1", 0.5f));
        dto.setOverlaps(overlaps);
        dto.setDominantBasket("Dominant");
        dto.setRiskFlag("High");
        assertEquals(overlaps, dto.getOverlaps());
        assertEquals("Dominant", dto.getDominantBasket());
        assertEquals("High", dto.getRiskFlag());
    }

    @Test
    public void testEqualsAndHashCode() {
        List<Overlap> overlaps = new ArrayList<>();
        overlaps.add(new Overlap("B", 0.5f));
        OverlapsInfo dto1 = new OverlapsInfo(overlaps, "D", "H");
        OverlapsInfo dto2 = new OverlapsInfo(overlaps, "D", "H");
        OverlapsInfo dto3 = new OverlapsInfo(new ArrayList<>(), "D", "H");
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        List<Overlap> overlaps = new ArrayList<>();
        overlaps.add(new Overlap("B", 0.5f));
        OverlapsInfo dto = new OverlapsInfo(overlaps, "D", "H");
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("D"));
    }
}
