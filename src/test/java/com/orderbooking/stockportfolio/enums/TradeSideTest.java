package com.orderbooking.stockportfolio.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TradeSideTest {

    @Test
    void shouldCoverGetCodeAndFromName() {
        assertEquals(1, TradeSide.BUY.getTradeSideCode());
        assertEquals(0, TradeSide.SELL.getTradeSideCode());
        assertEquals(TradeSide.BUY, TradeSide.fromName("buy"));
        assertEquals(TradeSide.SELL, TradeSide.fromName("SELL"));
        assertThrows(IllegalArgumentException.class, () -> TradeSide.fromName("invalid"));
    }
}
