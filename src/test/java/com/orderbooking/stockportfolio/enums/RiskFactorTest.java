package com.orderbooking.stockportfolio.enums;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskFactorTest {

    @Test
    void shouldCoverValuesAndPrivateField() throws Exception {
        assertArrayEquals(
                new RiskFactor[]{RiskFactor.HIGH, RiskFactor.MEDIUM, RiskFactor.LOW},
                RiskFactor.values()
        );

        Field field = RiskFactor.class.getDeclaredField("riskFactor");
        field.setAccessible(true);
        assertEquals(1, field.get(RiskFactor.HIGH));
        assertEquals(2, field.get(RiskFactor.MEDIUM));
        assertEquals(3, field.get(RiskFactor.LOW));
    }
}
