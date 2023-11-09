package com.shopbasket.inventoryservice.services.UpdateStock;

import com.shopbasket.inventoryservice.entities.Inventory;
import com.shopbasket.inventoryservice.repositories.InventoryRepository;
import com.shopbasket.inventoryservice.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateStockQuantityCommand implements InventoryCommand {
    @Autowired
    private InventoryService inventoryService;
    private InventoryRepository inventoryRepository;

    @Override
    public void execute(String inventoryBatchId, Integer quantity) {
        Inventory inventory = inventoryService.getInventory(inventoryBatchId);
        inventory.setQuantity(inventory.getQuantity()+ quantity);
        inventoryService.updateInventory(inventory);
    }
}