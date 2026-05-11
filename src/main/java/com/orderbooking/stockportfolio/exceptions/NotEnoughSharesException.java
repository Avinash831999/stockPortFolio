package com.orderbooking.stockportfolio.exceptions;

public class NotEnoughSharesException extends RuntimeException{
    public NotEnoughSharesException(String message) {
        super(message);
    }
}
