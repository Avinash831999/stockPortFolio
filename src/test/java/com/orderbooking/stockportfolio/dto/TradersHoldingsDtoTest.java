package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class TradersHoldingsDtoTest {

    @Test
    public void testNoArgsConstructor() {
        TradersHoldingsDto dto = new TradersHoldingsDto();
        assertNull(dto.getTraderId());
        assertNull(dto.getTraderName());
        assertNull(dto.getHoldings());
    }

    @Test
    public void testAllArgsConstructor() {
        List<HoldingStock> holdings = new ArrayList<>();
        holdings.add(new HoldingStock(1L, 2L, "Stock", 3L, "Sector", 100, new Date()));
        TradersHoldingsDto dto = new TradersHoldingsDto(1L, "Trader", holdings);
        assertEquals(1L, dto.getTraderId());
        assertEquals("Trader", dto.getTraderName());
        assertEquals(holdings, dto.getHoldings());
    }

    @Test
    public void testGettersAndSetters() {
        TradersHoldingsDto dto = new TradersHoldingsDto();
        List<HoldingStock> holdings = new ArrayList<>();
        holdings.add(new HoldingStock(1L, 2L, "Stock", 3L, "Sector", 100, new Date()));
        dto.setTraderId(1L);
        dto.setTraderName("Trader");
        dto.setHoldings(holdings);
        assertEquals(1L, dto.getTraderId());
        assertEquals("Trader", dto.getTraderName());
        assertEquals(holdings, dto.getHoldings());
    }

    @Test
    public void testEqualsAndHashCode() {
        List<HoldingStock> holdings = new ArrayList<>();
        holdings.add(new HoldingStock(1L, 2L, "S", 3L, "Sec", 100, new Date()));
        TradersHoldingsDto dto1 = new TradersHoldingsDto(1L, "T", holdings);
        TradersHoldingsDto dto2 = new TradersHoldingsDto(1L, "T", holdings);
        TradersHoldingsDto dto3 = new TradersHoldingsDto(2L, "T", holdings);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        List<HoldingStock> holdings = new ArrayList<>();
        holdings.add(new HoldingStock(1L, 2L, "S", 3L, "Sec", 100, new Date()));
        TradersHoldingsDto dto = new TradersHoldingsDto(1L, "T", holdings);
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("T"));
    }
}
