package com.shopbasket.inventoryservice.dto;

import com.shopbasket.inventoryservice.dto.CartItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

// CartDTO.java
public class CartDTO {
    private String id;
    private String warehouseKeeperID;
    private String deliveryManId;
    private List<CartItemDTO> cartItems;

}
