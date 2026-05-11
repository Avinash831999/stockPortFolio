package com.orderbooking.stockportfolio.enums;

public enum TraderStatus {
    ACTIVE(1),
    INACTIVE(0);


    private final Integer traderStatusCode;
    TraderStatus(Integer traderStatusCode) {
        this.traderStatusCode =  traderStatusCode;
    }
}
