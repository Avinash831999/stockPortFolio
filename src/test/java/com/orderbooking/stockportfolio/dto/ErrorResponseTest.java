package com.orderbooking.stockportfolio.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class ErrorResponseTest {

    @Test
    public void testConstructorWithStatusMessageError() {
        ErrorResponse dto = new ErrorResponse(400, "Bad Request", "Validation Error");
        assertEquals(400, dto.getStatus());
        assertEquals("Bad Request", dto.getMessage());
        assertEquals("Validation Error", dto.getError());
        assertNotNull(dto.getTimestamp());
        assertNull(dto.getPath());
    }

    @Test
    public void testConstructorWithStatusMessageErrorPath() {
        ErrorResponse dto = new ErrorResponse(400, "Bad Request", "Validation Error", "/api/test");
        assertEquals(400, dto.getStatus());
        assertEquals("Bad Request", dto.getMessage());
        assertEquals("Validation Error", dto.getError());
        assertNotNull(dto.getTimestamp());
        assertEquals("/api/test", dto.getPath());
    }

    @Test
    public void testGettersAndSetters() {
        ErrorResponse dto = new ErrorResponse(400, "Bad Request", "Validation Error");
        LocalDateTime timestamp = LocalDateTime.now();
        dto.setStatus(500);
        dto.setMessage("Internal Error");
        dto.setError("Server Error");
        dto.setTimestamp(timestamp);
        dto.setPath("/api/error");
        assertEquals(500, dto.getStatus());
        assertEquals("Internal Error", dto.getMessage());
        assertEquals("Server Error", dto.getError());
        assertEquals(timestamp, dto.getTimestamp());
        assertEquals("/api/error", dto.getPath());
    }

    @Test
    public void testEqualsAndHashCode() {
        ErrorResponse dto1 = new ErrorResponse(400, "Bad", "Error");
        ErrorResponse dto2 = new ErrorResponse(400, "Bad", "Error");
        ErrorResponse dto3 = new ErrorResponse(500, "Bad", "Error");
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        ErrorResponse dto = new ErrorResponse(400, "Bad", "Error");
        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("Bad"));
    }
}
