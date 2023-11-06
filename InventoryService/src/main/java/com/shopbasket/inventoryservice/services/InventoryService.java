package com.shopbasket.inventoryservice.services;

import com.shopbasket.inventoryservice.entities.Inventory;
import com.shopbasket.inventoryservice.repositories.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class InventoryService {

    private InventoryRepository inventoryRepository;
    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public Inventory getInventory(Long inventoryBatchId) {
        return inventoryRepository.findById(inventoryBatchId).orElse(null);
    }
    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public void deleteInventory(Long inventoryBatchId) {
        inventoryRepository.deleteById(inventoryBatchId);
    }


}
