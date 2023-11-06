package com.shopbasket.inventoryservice.Exceptions;

public class ProductUpdateException extends RuntimeException {
    public ProductUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
