package com.orderbooking.stockportfolio.enums;

public enum OrderStatus {
    PENDING(1),
    FILLED(2),
    CANCELLED(3);

    private final Integer orderStatus;

    OrderStatus(Integer orderStatus){
        this.orderStatus = orderStatus;
    }
}
