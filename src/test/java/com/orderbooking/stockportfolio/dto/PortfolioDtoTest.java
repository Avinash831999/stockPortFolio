package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

public class PortfolioDtoTest {

    @Test
    public void testNoArgsConstructor() {
        PortfolioDto dto = new PortfolioDto();
        assertNull(dto.getTraderId());
        assertNull(dto.getPositions());
        assertNull(dto.getSectorBreakDown());
    }

    @Test
    public void testAllArgsConstructor() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("Stock1", 100);
        Map<String, Integer> sectorBreakDown = new HashMap<>();
        sectorBreakDown.put("Sector1", 50);
        PortfolioDto dto = new PortfolioDto(1L, positions, sectorBreakDown);
        assertEquals(1L, dto.getTraderId());
        assertEquals(positions, dto.getPositions());
        assertEquals(sectorBreakDown, dto.getSectorBreakDown());
    }

    @Test
    public void testGettersAndSetters() {
        PortfolioDto dto = new PortfolioDto();
        Map<String, Integer> positions = new HashMap<>();
        positions.put("Stock1", 100);
        Map<String, Integer> sectorBreakDown = new HashMap<>();
        sectorBreakDown.put("Sector1", 50);
        dto.setTraderId(1L);
        dto.setPositions(positions);
        dto.setSectorBreakDown(sectorBreakDown);
        assertEquals(1L, dto.getTraderId());
        assertEquals(positions, dto.getPositions());
        assertEquals(sectorBreakDown, dto.getSectorBreakDown());
    }

    @Test
    public void testEqualsAndHashCode() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("S", 100);
        Map<String, Integer> sectorBreakDown = new HashMap<>();
        sectorBreakDown.put("Sec", 50);
        PortfolioDto dto1 = new PortfolioDto(1L, positions, sectorBreakDown);
        PortfolioDto dto2 = new PortfolioDto(1L, positions, sectorBreakDown);
        PortfolioDto dto3 = new PortfolioDto(2L, positions, sectorBreakDown);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        Map<String, Integer> positions = new HashMap<>();
        positions.put("S", 100);
        Map<String, Integer> sectorBreakDown = new HashMap<>();
        sectorBreakDown.put("Sec", 50);
        PortfolioDto dto = new PortfolioDto(1L, positions, sectorBreakDown);
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("1"));
    }
}
