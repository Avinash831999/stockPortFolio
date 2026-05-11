package com.orderbooking.stockportfolio.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxPendingOrdersCountExceptionTest {

    @Test
    void shouldSetMessage() {
        MaxPendingOrdersCountException exception = new MaxPendingOrdersCountException("max pending");
        assertEquals("max pending", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof RuntimeException);
    }
}
