package com.orderbooking.stockportfolio.enums;

public enum OrderStatus {
    PENDING(0),
    FILLED(1),
    CANCELLED(2);

    private final Integer orderStatus;

    OrderStatus(Integer orderStatus){
        this.orderStatus = orderStatus;
    }
}
