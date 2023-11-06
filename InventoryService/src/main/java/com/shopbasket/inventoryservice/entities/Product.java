package com.shopbasket.inventoryservice.entities;


import com.shopbasket.inventoryservice.enums.ProductCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    private String skuCode;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private ProductCategory productCategory;
    private String productImageUrl;
    private Date productAddedDate;

    @OneToMany(mappedBy = "product")
    private List<Inventory> inventoryList;



}
