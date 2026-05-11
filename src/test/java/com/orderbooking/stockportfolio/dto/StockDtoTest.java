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

public class StockDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        StockDto dto = new StockDto();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getPrice());
        assertNull(dto.getSectorId());
        assertNull(dto.getSectorName());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date created = new Date();
        Date updated = new Date();
        StockDto dto = new StockDto(1L, "Stock", 100.0f, 2L, "Sector", created, updated);
        assertEquals(1L, dto.getId());
        assertEquals("Stock", dto.getName());
        assertEquals(100.0f, dto.getPrice());
        assertEquals(2L, dto.getSectorId());
        assertEquals("Sector", dto.getSectorName());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        StockDto dto = new StockDto();
        Date created = new Date();
        Date updated = new Date();
        dto.setId(1L);
        dto.setName("Stock");
        dto.setPrice(100.0f);
        dto.setSectorId(2L);
        dto.setSectorName("Sector");
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals("Stock", dto.getName());
        assertEquals(100.0f, dto.getPrice());
        assertEquals(2L, dto.getSectorId());
        assertEquals("Sector", dto.getSectorName());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date created = new Date();
        Date updated = new Date();
        StockDto dto1 = new StockDto(1L, "S", 100.0f, 2L, "Sec", created, updated);
        StockDto dto2 = new StockDto(1L, "S", 100.0f, 2L, "Sec", created, updated);
        StockDto dto3 = new StockDto(2L, "S", 100.0f, 2L, "Sec", created, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        StockDto dto = new StockDto(1L, "S", 100.0f, 2L, "Sec", new Date(), new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("S"));
    }

    @Test
    public void testValidationValid() {
        StockDto dto = new StockDto(1L, "Stock", 100.0f, 2L, "Sector", new Date(), new Date());
        Set<ConstraintViolation<StockDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidName() {
        StockDto dto = new StockDto(1L, "", 100.0f, 2L, "Sector", new Date(), new Date());
        Set<ConstraintViolation<StockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("name", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidPrice() {
        StockDto dto = new StockDto(1L, "Stock", null, 2L, "Sector", new Date(), new Date());
        Set<ConstraintViolation<StockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("price", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidSectorId() {
        StockDto dto = new StockDto(1L, "Stock", 100.0f, null, "Sector", new Date(), new Date());
        Set<ConstraintViolation<StockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("sectorId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidMultiple() {
        StockDto dto = new StockDto(1L, "", null, null, "Sector", new Date(), new Date());
        Set<ConstraintViolation<StockDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }
}
