package com.shopbasket.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TotalAmountException.class)
    public ResponseEntity<String> handleOrderCreationException(TotalAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order creation failed: " + ex.getMessage());
    }

}
