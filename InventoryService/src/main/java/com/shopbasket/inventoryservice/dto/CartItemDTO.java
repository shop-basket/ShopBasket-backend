package com.shopbasket.inventoryservice.dto;

import com.shopbasket.inventoryservice.entities.Cart;
import com.shopbasket.inventoryservice.entities.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private String id;
    private String cartId;
    private List<String> inventoryIds; // List of inventory batch IDs
    private Integer orderedQuantity;
}
