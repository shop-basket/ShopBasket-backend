package com.shopbasket.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ProductQuantityDetailsResponse {
    private String skuCode;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productCategory;
    private String productImageUrl;
    private String productAddedDate;
    private String inventoryStatus;
    private Integer quantity;
}
