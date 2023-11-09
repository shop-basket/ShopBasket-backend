package com.shopbasket.inventoryservice.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopbasket.inventoryservice.enums.InventoryStatus;
import com.shopbasket.inventoryservice.enums.ProductCategory;
import jakarta.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;
    private String productImageUrl;
    private Date productAddedDate;
    @Enumerated(EnumType.STRING)
    private InventoryStatus inventoryStatus;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Inventory> inventoryList;



}
