package com.shopbasket.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder

public class OrderItemDTO {
    private String skuCode;
    private int orderedQuantity;

}