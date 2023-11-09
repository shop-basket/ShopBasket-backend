package com.shopbasket.inventoryservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CartItem {
    @Id
    //Need to create the ID's from orderID
    private String id;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private Integer orderedQuantity;

}