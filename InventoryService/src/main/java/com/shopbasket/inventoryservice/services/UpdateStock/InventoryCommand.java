package com.shopbasket.inventoryservice.services.UpdateStock;

public interface InventoryCommand {
    void execute(String inventoryBatchId, Integer quantity);
}