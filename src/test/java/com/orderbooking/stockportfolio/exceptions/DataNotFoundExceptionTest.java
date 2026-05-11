package com.orderbooking.stockportfolio.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataNotFoundExceptionTest {

    @Test
    void shouldSetMessage() {
        DataNotFoundException exception = new DataNotFoundException("not found");
        assertEquals("not found", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }
}
