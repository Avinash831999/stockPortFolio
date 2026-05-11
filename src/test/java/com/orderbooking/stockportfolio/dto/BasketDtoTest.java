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

public class BasketDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        BasketDto dto = new BasketDto();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getBasketStatus());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date created = new Date();
        Date updated = new Date();
        BasketDto dto = new BasketDto(1L, "Test Basket", "ACTIVE", created, updated);
        assertEquals(1L, dto.getId());
        assertEquals("Test Basket", dto.getName());
        assertEquals("ACTIVE", dto.getBasketStatus());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        BasketDto dto = new BasketDto();
        Date created = new Date();
        Date updated = new Date();
        dto.setId(1L);
        dto.setName("Test Basket");
        dto.setBasketStatus("ACTIVE");
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals("Test Basket", dto.getName());
        assertEquals("ACTIVE", dto.getBasketStatus());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date created = new Date();
        Date updated = new Date();
        BasketDto dto1 = new BasketDto(1L, "Test", "ACTIVE", created, updated);
        BasketDto dto2 = new BasketDto(1L, "Test", "ACTIVE", created, updated);
        BasketDto dto3 = new BasketDto(2L, "Test", "ACTIVE", created, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        BasketDto dto = new BasketDto(1L, "Test", "ACTIVE", new Date(), new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("Test"));
    }

    @Test
    public void testValidationValid() {
        BasketDto dto = new BasketDto(1L, "Test Basket", "ACTIVE", new Date(), new Date());
        Set<ConstraintViolation<BasketDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidName() {
        BasketDto dto = new BasketDto(1L, "", "ACTIVE", new Date(), new Date());
        Set<ConstraintViolation<BasketDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("name", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidBasketStatus() {
        BasketDto dto = new BasketDto(1L, "Test", "", new Date(), new Date());
        Set<ConstraintViolation<BasketDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("basketStatus", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidBoth() {
        BasketDto dto = new BasketDto(1L, "", "", new Date(), new Date());
        Set<ConstraintViolation<BasketDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    }
}
