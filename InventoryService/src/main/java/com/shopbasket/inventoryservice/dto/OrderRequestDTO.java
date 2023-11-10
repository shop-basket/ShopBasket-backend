package com.shopbasket.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private String orderId;
    private List<OrderItemDTO> orderItems;
}
