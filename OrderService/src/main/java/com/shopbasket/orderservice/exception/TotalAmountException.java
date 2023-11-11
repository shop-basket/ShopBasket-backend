package com.shopbasket.orderservice.exception;

public class TotalAmountException extends RuntimeException {
    public TotalAmountException(String message) {
        super(message);
    }
}
