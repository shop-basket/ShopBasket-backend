package com.shopbasket.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderRequestDTO {
    private String orderId;
    private String warehouseKeeperID;
    private String deliveryManId;
    private List<OrderItemDTO> orderItems;
}
