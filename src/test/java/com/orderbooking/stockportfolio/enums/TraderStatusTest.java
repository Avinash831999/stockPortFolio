package com.orderbooking.stockportfolio.enums;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TraderStatusTest {

    @Test
    void shouldCoverValuesAndPrivateField() throws Exception {
        assertArrayEquals(
                new TraderStatus[]{TraderStatus.ACTIVE, TraderStatus.INACTIVE},
                TraderStatus.values()
        );

        Field field = TraderStatus.class.getDeclaredField("traderStatusCode");
        field.setAccessible(true);
        assertEquals(0, field.get(TraderStatus.ACTIVE));
        assertEquals(1, field.get(TraderStatus.INACTIVE));
    }
}
