package com.shopbasket.inventoryservice.repositories;

import com.shopbasket.inventoryservice.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.product.skuCode = :skuCode")
    Integer sumQuantityBySkuCode(@Param("skuCode") String skuCode);
    @Query("SELECT i FROM Inventory i WHERE i.product.skuCode = :skuCode ORDER BY i.expiryDate")
    List<Inventory> findInventoriesBySkuCodeOrderByExpiryDate(@Param("skuCode") String skuCode);


}
