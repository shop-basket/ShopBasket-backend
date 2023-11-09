package com.shopbasket.inventoryservice.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopbasket.inventoryservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Cart {
    @Id
    private String id;
    private String warehouseKeeperID;
    private String deliveryManId;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> cartItems;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



}