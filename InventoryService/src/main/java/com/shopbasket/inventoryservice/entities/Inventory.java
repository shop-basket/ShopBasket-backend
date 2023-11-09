package com.shopbasket.inventoryservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shopbasket.inventoryservice.config.InventoryEntityListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "inventory")
@EntityListeners(InventoryEntityListener.class)
public class Inventory {
    @Id
    private String inventoryBatchId;
    @ManyToOne
    @JoinColumn(name = "skuCode")
    @JsonBackReference
    private Product product;
    private Integer quantity;
    private Date manufacturingDate;
    private Date expiryDate;
    private String warehouseKeeperID;

}
