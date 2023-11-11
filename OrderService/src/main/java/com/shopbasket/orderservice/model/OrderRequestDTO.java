package com.shopbasket.orderservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private Long orderId;
    private List<OrderItemDTO> orderItems;
}