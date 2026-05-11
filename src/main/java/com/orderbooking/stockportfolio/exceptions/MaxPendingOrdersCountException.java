package com.orderbooking.stockportfolio.exceptions;

public class MaxPendingOrdersCountException extends RuntimeException{
    public MaxPendingOrdersCountException(String message) {
        super(message);
    }
}
