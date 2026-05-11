package com.orderbooking.stockportfolio.enums;

public enum BasketStatus {

    ACTIVE(1),
    INACTIVE(0);

    private final Integer basketStatusCode;

    BasketStatus(Integer basketStatusCode) {
        this.basketStatusCode = basketStatusCode;
    }

    public Integer getBasketStatusCode() {
        return basketStatusCode;
    }

    public static BasketStatus fromName(String name) {
        return BasketStatus.valueOf(name.toUpperCase());
    }
}