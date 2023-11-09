package com.shopbasket.inventoryservice.config;

import com.shopbasket.inventoryservice.entities.Inventory;
import com.shopbasket.inventoryservice.enums.InventoryStatus;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
// trigger to update the inventory status of the product

public class InventoryEntityListener {

    @PostPersist
    @PostUpdate
    public void updateProductInventoryStatus(Inventory inventory) {
        int quantity = inventory.getQuantity();
        System.out.println("quantity = " + quantity);
        if (quantity > 0) {
            inventory.getProduct().setInventoryStatus(InventoryStatus.IN_STOCK);
        } else {
            inventory.getProduct().setInventoryStatus(InventoryStatus.OUT_OF_STOCK);
        }
    }
}
