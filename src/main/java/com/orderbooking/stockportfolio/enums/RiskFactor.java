package com.orderbooking.stockportfolio.enums;

public enum RiskFactor {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    private final Integer riskFactor;

    RiskFactor(Integer riskFactor) {
        this.riskFactor = riskFactor;
    }
}
