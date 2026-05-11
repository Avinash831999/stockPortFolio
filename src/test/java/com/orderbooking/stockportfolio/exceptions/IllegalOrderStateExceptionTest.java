package com.orderbooking.stockportfolio.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IllegalOrderStateExceptionTest {

    @Test
    void shouldSetMessage() {
        IllegalOrderStateException exception = new IllegalOrderStateException("illegal state");
        assertEquals("illegal state", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }
}
