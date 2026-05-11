package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import java.util.Date;

public class StockBasketMapDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        StockBasketMapDto dto = new StockBasketMapDto();
        assertNull(dto.getId());
        assertNull(dto.getBasketId());
        assertNull(dto.getBasketName());
        assertNull(dto.getStockName());
        assertNull(dto.getStockId());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date created = new Date();
        Date updated = new Date();
        StockBasketMapDto dto = new StockBasketMapDto(1L, 2L, "Basket", "Stock", 3L, created, updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getBasketId());
        assertEquals("Basket", dto.getBasketName());
        assertEquals("Stock", dto.getStockName());
        assertEquals(3L, dto.getStockId());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        StockBasketMapDto dto = new StockBasketMapDto();
        Date created = new Date();
        Date updated = new Date();
        dto.setId(1L);
        dto.setBasketId(2L);
        dto.setBasketName("Basket");
        dto.setStockName("Stock");
        dto.setStockId(3L);
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getBasketId());
        assertEquals("Basket", dto.getBasketName());
        assertEquals("Stock", dto.getStockName());
        assertEquals(3L, dto.getStockId());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date created = new Date();
        Date updated = new Date();
        StockBasketMapDto dto1 = new StockBasketMapDto(1L, 2L, "B", "S", 3L, created, updated);
        StockBasketMapDto dto2 = new StockBasketMapDto(1L, 2L, "B", "S", 3L, created, updated);
        StockBasketMapDto dto3 = new StockBasketMapDto(2L, 2L, "B", "S", 3L, created, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        StockBasketMapDto dto = new StockBasketMapDto(1L, 2L, "B", "S", 3L, new Date(), new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("B"));
    }

    @Test
    public void testValidationValid() {
        StockBasketMapDto dto = new StockBasketMapDto(1L, 2L, "Basket", "Stock", 3L, new Date(), new Date());
        Set<ConstraintViolation<StockBasketMapDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidBasketId() {
        StockBasketMapDto dto = new StockBasketMapDto(1L, null, "Basket", "Stock", 3L, new Date(), new Date());
        Set<ConstraintViolation<StockBasketMapDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("basketId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidStockId() {
        StockBasketMapDto dto = new StockBasketMapDto(1L, 2L, "Basket", "Stock", null, new Date(), new Date());
        Set<ConstraintViolation<StockBasketMapDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("stockId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidBoth() {
        StockBasketMapDto dto = new StockBasketMapDto(1L, null, "Basket", "Stock", null, new Date(), new Date());
        Set<ConstraintViolation<StockBasketMapDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }
}
