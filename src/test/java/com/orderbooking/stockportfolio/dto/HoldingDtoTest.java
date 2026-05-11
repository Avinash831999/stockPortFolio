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

public class HoldingDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        HoldingDto dto = new HoldingDto();
        assertNull(dto.getId());
        assertNull(dto.getTraderId());
        assertNull(dto.getStockId());
        assertNull(dto.getSectorId());
        assertNull(dto.getQuantity());
        assertNull(dto.getBasketId());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date updated = new Date();
        HoldingDto dto = new HoldingDto(1L, 2L, 3L, 4L, 100, 5L, updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getTraderId());
        assertEquals(3L, dto.getStockId());
        assertEquals(4L, dto.getSectorId());
        assertEquals(100, dto.getQuantity());
        assertEquals(5L, dto.getBasketId());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        HoldingDto dto = new HoldingDto();
        Date updated = new Date();
        dto.setId(1L);
        dto.setTraderId(2L);
        dto.setStockId(3L);
        dto.setSectorId(4L);
        dto.setQuantity(100);
        dto.setBasketId(5L);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getTraderId());
        assertEquals(3L, dto.getStockId());
        assertEquals(4L, dto.getSectorId());
        assertEquals(100, dto.getQuantity());
        assertEquals(5L, dto.getBasketId());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date updated = new Date();
        HoldingDto dto1 = new HoldingDto(1L, 2L, 3L, 4L, 100, 5L, updated);
        HoldingDto dto2 = new HoldingDto(1L, 2L, 3L, 4L, 100, 5L, updated);
        HoldingDto dto3 = new HoldingDto(2L, 2L, 3L, 4L, 100, 5L, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        HoldingDto dto = new HoldingDto(1L, 2L, 3L, 4L, 100, 5L, new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("100"));
    }

    @Test
    public void testValidationValid() {
        HoldingDto dto = new HoldingDto(1L, 2L, 3L, 4L, 100, 5L, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidTraderId() {
        HoldingDto dto = new HoldingDto(1L, null, 3L, 4L, 100, 5L, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("traderId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidStockId() {
        HoldingDto dto = new HoldingDto(1L, 2L, null, 4L, 100, 5L, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("stockId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidSectorId() {
        HoldingDto dto = new HoldingDto(1L, 2L, 3L, null, 100, 5L, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("sectorId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidQuantity() {
        HoldingDto dto = new HoldingDto(1L, 2L, 3L, 4L, null, 5L, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("quantity", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidBasketId() {
        HoldingDto dto = new HoldingDto(1L, 2L, 3L, 4L, 100, null, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("basketId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidMultiple() {
        HoldingDto dto = new HoldingDto(1L, null, null, null, null, null, new Date());
        Set<ConstraintViolation<HoldingDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
    }
}
