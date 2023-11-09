package com.ShopBasket.deliveryservice.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long orderId;
    private String deliveryAddress;
    private double amount;



    // Constructors, getters, and setters
}

