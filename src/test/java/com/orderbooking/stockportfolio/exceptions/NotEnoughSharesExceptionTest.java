package com.orderbooking.stockportfolio.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotEnoughSharesExceptionTest {

    @Test
    void shouldSetMessage() {
        NotEnoughSharesException exception = new NotEnoughSharesException("not enough shares");
        assertEquals("not enough shares", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }
}
