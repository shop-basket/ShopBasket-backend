package com.shopbasket.inventoryservice.dto;

import com.shopbasket.inventoryservice.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Adapter class for InventoryRequestDTO
public class InventoryRequestDTO {

    private String skuCode;
    private Integer quantity;
    private String locationCode;
    private String inventoryStatus;
    private Date manufacturingDate;
    private Date expiryDate;
    private String warehouseKeeperID;
}
