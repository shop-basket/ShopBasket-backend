package com.shopbasket.inventoryservice.entities;

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
public class Inventory {
    @Id
    private Long inventoryBatchId;
    @ManyToOne
    @JoinColumn(name = "skuCode")
    private Product product;
    private Integer quantity;
    private String locationCode;
    private String inventoryStatus;
    private Date ManufacturingDate;
    private Date expiryDate;
    private String warehouseKeeperID;

}
