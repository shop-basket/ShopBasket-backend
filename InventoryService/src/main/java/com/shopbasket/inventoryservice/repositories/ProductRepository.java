package com.shopbasket.inventoryservice.repositories;

import com.shopbasket.inventoryservice.entities.Product;
import com.shopbasket.inventoryservice.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    @Query("SELECT p FROM Product p WHERE p.productCategory = :category")
    List<Product> findByCategory(@Param("category") ProductCategory category);
}
