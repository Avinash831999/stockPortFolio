package com.orderbooking.stockportfolio.enums;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusTest {

    @Test
    void shouldCoverValuesAndPrivateField() throws Exception {
        assertArrayEquals(
                new OrderStatus[]{OrderStatus.PENDING, OrderStatus.FILLED, OrderStatus.CANCELLED},
                OrderStatus.values()
        );

        Field field = OrderStatus.class.getDeclaredField("orderStatus");
        field.setAccessible(true);
        assertEquals(1, field.get(OrderStatus.PENDING));
        assertEquals(2, field.get(OrderStatus.FILLED));
        assertEquals(3, field.get(OrderStatus.CANCELLED));
    }
}
