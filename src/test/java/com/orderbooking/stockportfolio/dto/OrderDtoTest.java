package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.orderbooking.stockportfolio.enums.OrderStatus;
import com.orderbooking.stockportfolio.enums.TradeSide;
import java.util.Date;

public class OrderDtoTest {

    @Test
    public void testNoArgsConstructor() {
        OrderDto dto = new OrderDto();
        assertNull(dto.getId());
        assertNull(dto.getTraderId());
        assertNull(dto.getTraderName());
        assertNull(dto.getStockId());
        assertNull(dto.getStockName());
        assertNull(dto.getSectorId());
        assertNull(dto.getSectorName());
        assertNull(dto.getQuantity());
        assertNull(dto.getRate());
        assertNull(dto.getTotal());
        assertNull(dto.getSide());
        assertNull(dto.getOrderStatus());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date created = new Date();
        Date updated = new Date();
        OrderDto dto = new OrderDto(1L, 2L, "Trader", 3L, "Stock", 4L, "Sector", 100, 50.0f, 5000.0f, TradeSide.BUY, OrderStatus.PENDING, created, updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getTraderId());
        assertEquals("Trader", dto.getTraderName());
        assertEquals(3L, dto.getStockId());
        assertEquals("Stock", dto.getStockName());
        assertEquals(4L, dto.getSectorId());
        assertEquals("Sector", dto.getSectorName());
        assertEquals(100, dto.getQuantity());
        assertEquals(50.0f, dto.getRate());
        assertEquals(5000.0f, dto.getTotal());
        assertEquals(TradeSide.BUY, dto.getSide());
        assertEquals(OrderStatus.PENDING, dto.getOrderStatus());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        OrderDto dto = new OrderDto();
        Date created = new Date();
        Date updated = new Date();
        dto.setId(1L);
        dto.setTraderId(2L);
        dto.setTraderName("Trader");
        dto.setStockId(3L);
        dto.setStockName("Stock");
        dto.setSectorId(4L);
        dto.setSectorName("Sector");
        dto.setQuantity(100);
        dto.setRate(50.0f);
        dto.setTotal(5000.0f);
        dto.setSide(TradeSide.BUY);
        dto.setOrderStatus(OrderStatus.PENDING);
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getTraderId());
        assertEquals("Trader", dto.getTraderName());
        assertEquals(3L, dto.getStockId());
        assertEquals("Stock", dto.getStockName());
        assertEquals(4L, dto.getSectorId());
        assertEquals("Sector", dto.getSectorName());
        assertEquals(100, dto.getQuantity());
        assertEquals(50.0f, dto.getRate());
        assertEquals(5000.0f, dto.getTotal());
        assertEquals(TradeSide.BUY, dto.getSide());
        assertEquals(OrderStatus.PENDING, dto.getOrderStatus());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date created = new Date();
        Date updated = new Date();
        OrderDto dto1 = new OrderDto(1L, 2L, "T", 3L, "S", 4L, "Sec", 100, 50.0f, 5000.0f, TradeSide.BUY, OrderStatus.PENDING, created, updated);
        OrderDto dto2 = new OrderDto(1L, 2L, "T", 3L, "S", 4L, "Sec", 100, 50.0f, 5000.0f, TradeSide.BUY, OrderStatus.PENDING, created, updated);
        OrderDto dto3 = new OrderDto(2L, 2L, "T", 3L, "S", 4L, "Sec", 100, 50.0f, 5000.0f, TradeSide.BUY, OrderStatus.PENDING, created, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        OrderDto dto = new OrderDto(1L, 2L, "T", 3L, "S", 4L, "Sec", 100, 50.0f, 5000.0f, TradeSide.BUY, OrderStatus.PENDING, new Date(), new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("T"));
    }
}
