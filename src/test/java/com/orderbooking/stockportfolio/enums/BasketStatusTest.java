package com.orderbooking.stockportfolio.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasketStatusTest {

    @Test
    void shouldCoverGetCodeAndFromName() {
        assertEquals(1, BasketStatus.ACTIVE.getBasketStatusCode());
        assertEquals(0, BasketStatus.INACTIVE.getBasketStatusCode());
        assertEquals(BasketStatus.ACTIVE, BasketStatus.fromName("active"));
        assertEquals(BasketStatus.INACTIVE, BasketStatus.fromName("INACTIVE"));
        assertThrows(IllegalArgumentException.class, () -> BasketStatus.fromName("invalid"));
    }
}
