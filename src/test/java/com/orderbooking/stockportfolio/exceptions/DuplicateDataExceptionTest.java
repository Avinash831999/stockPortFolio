package com.orderbooking.stockportfolio.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DuplicateDataExceptionTest {

    @Test
    void shouldSetMessage() {
        DuplicateDataException exception = new DuplicateDataException("duplicate");
        assertEquals("duplicate", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }
}
