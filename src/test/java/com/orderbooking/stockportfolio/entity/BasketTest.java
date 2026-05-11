package com.orderbooking.stockportfolio.entity;

import com.orderbooking.stockportfolio.enums.BasketStatus;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasketTest {

    @Test
    void shouldCoverConstructorsAccessorsAndCommonMethods() {
        Basket empty = new Basket();
        assertNull(empty.getId());
        assertNull(empty.getName());
        assertNull(empty.getBasketStatus());
        assertNull(empty.getCreatedAt());
        assertNull(empty.getUpdatedAt());

        Date createdAt = new Date();
        Date updatedAt = new Date();
        Basket basket = new Basket(1L, "Growth", BasketStatus.ACTIVE, createdAt, updatedAt);
        assertEquals(1L, basket.getId());
        assertEquals("Growth", basket.getName());
        assertEquals(BasketStatus.ACTIVE, basket.getBasketStatus());
        assertEquals(createdAt, basket.getCreatedAt());
        assertEquals(updatedAt, basket.getUpdatedAt());

        Basket same = new Basket(1L, "Growth", BasketStatus.ACTIVE, createdAt, updatedAt);
        assertEquals(basket, same);
        assertEquals(basket.hashCode(), same.hashCode());
        assertTrue(basket.toString().contains("Growth"));
    }
}
