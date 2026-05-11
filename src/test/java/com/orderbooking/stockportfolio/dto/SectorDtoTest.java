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

public class SectorDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        SectorDto dto = new SectorDto();
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    public void testAllArgsConstructor() {
        Date created = new Date();
        Date updated = new Date();
        SectorDto dto = new SectorDto(1L, "Test Sector", created, updated);
        assertEquals(1L, dto.getId());
        assertEquals("Test Sector", dto.getName());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testGettersAndSetters() {
        SectorDto dto = new SectorDto();
        Date created = new Date();
        Date updated = new Date();
        dto.setId(1L);
        dto.setName("Test Sector");
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);
        assertEquals(1L, dto.getId());
        assertEquals("Test Sector", dto.getName());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    public void testEqualsAndHashCode() {
        Date created = new Date();
        Date updated = new Date();
        SectorDto dto1 = new SectorDto(1L, "Test", created, updated);
        SectorDto dto2 = new SectorDto(1L, "Test", created, updated);
        SectorDto dto3 = new SectorDto(2L, "Test", created, updated);
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        SectorDto dto = new SectorDto(1L, "Test", new Date(), new Date());
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("Test"));
    }

    @Test
    public void testValidationValid() {
        SectorDto dto = new SectorDto(1L, "Test Sector", new Date(), new Date());
        Set<ConstraintViolation<SectorDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidName() {
        SectorDto dto = new SectorDto(1L, "", new Date(), new Date());
        Set<ConstraintViolation<SectorDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("name", violations.iterator().next().getPropertyPath().toString());
    }
}
