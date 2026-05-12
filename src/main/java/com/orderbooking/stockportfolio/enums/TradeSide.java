package com.orderbooking.stockportfolio.enums;

public enum TradeSide {

    BUY (0),
    SELL(1);

    private final Integer tradeSideCode;

    TradeSide(Integer tradeSideCode){
        this.tradeSideCode = tradeSideCode;
    }

    public Integer getTradeSideCode() {
        return tradeSideCode;
    }


    public static TradeSide fromName(String name) {
        return TradeSide.valueOf(name.toUpperCase());
    }

}
