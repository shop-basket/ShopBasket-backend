package com.shopbasket.inventoryservice.dto;

import com.shopbasket.inventoryservice.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
//Adapter class for ProductRequest

public class ProductRequest {
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String skuCode;
    private ProductCategory productCategory;
    private String productImageUrl;
    private Date productAddedDate;
}
