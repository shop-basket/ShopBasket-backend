package com.shopbasket.inventoryservice.controllers;

import com.shopbasket.inventoryservice.dto.InventoryRequestDTO;
import com.shopbasket.inventoryservice.dto.OrderRequestDTO;
import com.shopbasket.inventoryservice.dto.ProductQuantityDetailsResponse;
import com.shopbasket.inventoryservice.services.InventoryService;
import com.shopbasket.inventoryservice.services.UpdateStock.InventoryCommand;
import com.shopbasket.inventoryservice.services.UpdateStock.UpdateStockQuantityCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ShopBasket/api/inventory")

public class InventoryController {
    @Autowired
    private InventoryService inventoryService;
    private UpdateStockQuantityCommand updateStockQuantityCommand;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) {
        System.out.println("Inventory Request DTO: " + inventoryRequestDTO);
        inventoryService.addInventory(inventoryRequestDTO);
        return ResponseEntity.ok("Inventory has been successfully added.");
    }

    @PutMapping({"/{inventoryBatchId}/updateStockQuantity/{quantity}"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateStockQuantity(@PathVariable String inventoryBatchId, @PathVariable Integer quantity) {
//        inventoryService.updateStockQuantity(inventoryBatchId, quantity);
        updateStockQuantityCommand.execute(inventoryBatchId, quantity);
        return ResponseEntity.ok("Inventory with inventoryBatchId " + inventoryBatchId + " has been successfully updated.");
    }


    @GetMapping("/{inventoryBatchId}/getCurrentQuantity")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCurrentQuantity(@PathVariable String inventoryBatchId) {
        return inventoryService.getCurrentQuantity(inventoryBatchId);

    }

    @GetMapping("/totalQuantity/{skuCode}")
    public Integer getTotalQuantityBySkuCode(@PathVariable String skuCode) {
        Integer totalQuantity = inventoryService.calculateTotalQuantityBySkuCode(skuCode);
        return totalQuantity;
    }

    //Product Details with Quantity
    @GetMapping("/view/{skuCode}")
    public ProductQuantityDetailsResponse getProductQuantityDetails(@PathVariable String skuCode) {
        return inventoryService.getProductQuantityDetails(skuCode);
    }

    @PostMapping("/check-order")
    public ResponseEntity<String> prepareAndCheckOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        // Check if all ordered items are in stock
        if (inventoryService.areItemsInStock(orderRequestDTO)) {
//            inventoryService.prepareOrder(orderRequestDTO);
            return ResponseEntity.ok("Order is prepared and ready for further processing.");
        } else {
            return ResponseEntity.ok("Some items in the order are out of stock.");
        }
    }
    @PostMapping("/processOrder")  // Define the endpoint URL for order processing
    public void processOrder(@RequestBody OrderRequestDTO orderRequest) {
        inventoryService.processOrderV1(orderRequest);
    }



}
