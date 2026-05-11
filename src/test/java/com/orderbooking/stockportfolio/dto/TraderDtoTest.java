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

public class TraderDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        TraderDto dto = new TraderDto();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getPanNumber());
        assertNull(dto.getTraderStatus());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdated_at());
    }

    @Test
    public void testAllArgsConstructor() {
        Date created = new Date();
        Date updated = new Date();
        TraderDto dto = new TraderDto(1L, "John", "john@example.com", "ABC123", "ACTIVE", created, updated);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("ABC123", dto.getPanNumber());
        assertEquals("ACTIVE", dto.getTraderStatus());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdated_at());
    }

    @Test
    public void testGettersAndSetters() {
        TraderDto dto = new TraderDto();
        Date created = new Date();
        Date updated = new Date();
        dto.setId(1L);
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPanNumber("ABC123");
        dto.setTraderStatus("ACTIVE");
        dto.setCreatedAt(created);
        dto.setUpdated_at(updated);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
        assertEquals("ABC123", dto.getPanNumber());
        assertEquals("ACTIVE", dto.getTraderStatus());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdated_at());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date created = new Date();
        Date updated = new Date();
        TraderDto dto1 = new TraderDto(1L, "J", "j@e.com", "A", "A", created, updated);
        TraderDto dto2 = new TraderDto(1L, "J", "j@e.com", "A", "A", created, updated);
        TraderDto dto3 = new TraderDto(2L, "J", "j@e.com", "A", "A", created, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        TraderDto dto = new TraderDto(1L, "J", "j@e.com", "A", "A", new Date(), new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("J"));
    }

    @Test
    public void testValidationValid() {
        TraderDto dto = new TraderDto(1L, "John", "john@example.com", "ABC123", "ACTIVE", new Date(), new Date());
        Set<ConstraintViolation<TraderDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidName() {
        TraderDto dto = new TraderDto(1L, "", "john@example.com", "ABC123", "ACTIVE", new Date(), new Date());
        Set<ConstraintViolation<TraderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("name", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidEmail() {
        TraderDto dto = new TraderDto(1L, "John", "", "ABC123", "ACTIVE", new Date(), new Date());
        Set<ConstraintViolation<TraderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("email", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidPanNumber() {
        TraderDto dto = new TraderDto(1L, "John", "john@example.com", "", "ACTIVE", new Date(), new Date());
        Set<ConstraintViolation<TraderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("panNumber", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidTraderStatus() {
        TraderDto dto = new TraderDto(1L, "John", "john@example.com", "ABC123", "", new Date(), new Date());
        Set<ConstraintViolation<TraderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("traderStatus", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidMultiple() {
        TraderDto dto = new TraderDto(1L, "", "", "", "", new Date(), new Date());
        Set<ConstraintViolation<TraderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());
    }
}
