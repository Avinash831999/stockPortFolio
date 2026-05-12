package com.orderbooking.stockportfolio.enums;

public enum TraderStatus {
    ACTIVE(0),
    INACTIVE(1);


    private final Integer traderStatusCode;
    TraderStatus(Integer traderStatusCode) {
        this.traderStatusCode =  traderStatusCode;
    }
}
