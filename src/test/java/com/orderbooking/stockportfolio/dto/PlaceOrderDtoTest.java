package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class PlaceOrderDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testNoArgsConstructor() {
        PlaceOrderDto dto = new PlaceOrderDto();
        assertNull(dto.getTraderId());
        assertNull(dto.getStockId());
        assertNull(dto.getSectorId());
        assertNull(dto.getQuantity());
        assertNull(dto.getSide());
    }

    @Test
    public void testAllArgsConstructor() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, 10, "BUY");
        assertEquals(1L, dto.getTraderId());
        assertEquals(2L, dto.getStockId());
        assertEquals(3L, dto.getSectorId());
        assertEquals(10, dto.getQuantity());
        assertEquals("BUY", dto.getSide());
    }

    @Test
    public void testGettersAndSetters() {
        PlaceOrderDto dto = new PlaceOrderDto();
        dto.setTraderId(1L);
        dto.setStockId(2L);
        dto.setSectorId(3L);
        dto.setQuantity(10);
        dto.setSide("BUY");
        assertEquals(1L, dto.getTraderId());
        assertEquals(2L, dto.getStockId());
        assertEquals(3L, dto.getSectorId());
        assertEquals(10, dto.getQuantity());
        assertEquals("BUY", dto.getSide());
    }

    @Test
    public void testEqualsAndHashCode() {
        PlaceOrderDto dto1 = new PlaceOrderDto(1L, 2L, 3L, 10, "BUY");
        PlaceOrderDto dto2 = new PlaceOrderDto(1L, 2L, 3L, 10, "BUY");
        PlaceOrderDto dto3 = new PlaceOrderDto(2L, 2L, 3L, 10, "BUY");
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, 10, "BUY");
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("BUY"));
    }

    @Test
    public void testValidationValid() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, 10, "BUY");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidationInvalidTraderId() {
        PlaceOrderDto dto = new PlaceOrderDto(null, 2L, 3L, 10, "BUY");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("traderId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidStockId() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, null, 3L, 10, "BUY");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("stockId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidSectorId() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, null, 10, "BUY");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("sectorId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidQuantity() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, null, "BUY");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("quantity", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidSide() {
        PlaceOrderDto dto = new PlaceOrderDto(1L, 2L, 3L, 10, "");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("side", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    public void testValidationInvalidMultiple() {
        PlaceOrderDto dto = new PlaceOrderDto(null, null, null, null, "");
        Set<ConstraintViolation<PlaceOrderDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(5, violations.size());
    }
}
