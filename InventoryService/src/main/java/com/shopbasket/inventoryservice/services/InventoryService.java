package com.shopbasket.inventoryservice.services;
import com.shopbasket.inventoryservice.dto.*;
import com.shopbasket.inventoryservice.entities.Inventory;
import com.shopbasket.inventoryservice.entities.Product;
import com.shopbasket.inventoryservice.enums.InventoryStatus;
import com.shopbasket.inventoryservice.repositories.InventoryRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
@NoArgsConstructor
@Data
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private ProductService productService;
    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, ProductService productService) {
        this.inventoryRepository = inventoryRepository;
        this.productService = productService;
    }

    // Bussiness Logic for Adding a Inventory
    // This method is called by the InventoryController to add a new inventory.
    public void addInventory(InventoryRequestDTO inventoryRequestDTO) {
        String randomString = RandomStringUtils.randomAlphabetic(2, 3); // Generate a random string of length 2 or 3
        String inventoryBatchId = inventoryRequestDTO.getSkuCode() +"_"+ randomString;// Generate a unique inventory batch ID
        String inventoryStatus = (inventoryRequestDTO.getQuantity() > 0) ? "IN_STOCK" : "OUT_OF_STOCK";// Set the inventory status based on the quantity
        Inventory inventory=Inventory.builder()
                .inventoryBatchId(inventoryBatchId)
                .quantity(inventoryRequestDTO.getQuantity())
                .manufacturingDate(inventoryRequestDTO.getManufacturingDate())
                .expiryDate(inventoryRequestDTO.getExpiryDate())
                .warehouseKeeperID(inventoryRequestDTO.getWarehouseKeeperID())
                .product(productService.getProduct(inventoryRequestDTO.getSkuCode()))
                .build();
        inventoryRepository.save(inventory);
        Product product = productService.getProduct(inventoryRequestDTO.getSkuCode());
        product.setInventoryStatus(InventoryStatus.valueOf(inventoryStatus));

        // Check if total quantity is 0, set InventoryStatus to OUT_OF_STOCK
        if (calculateTotalQuantityBySkuCode(inventoryRequestDTO.getSkuCode()) > 0) {
            product.setInventoryStatus(InventoryStatus.IN_STOCK);
        }
    }
    public Inventory getInventory(String inventoryBatchId) {
        return inventoryRepository.findById(inventoryBatchId).orElse(null);
    }
    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public void deleteInventory(String inventoryBatchId) {
        inventoryRepository.deleteById(inventoryBatchId);
    }


    public void updateStockQuantity(String inventoryBatchId, Integer quantity) {
        Inventory inventory = getInventory(inventoryBatchId);
        inventory.setQuantity(inventory.getQuantity()+ quantity);
        updateInventory(inventory);
    }
    public int getCurrentQuantity(String inventoryBatchId )
    {
        Inventory inventory = getInventory(inventoryBatchId);
        return inventory.getQuantity();
    }

    public Integer calculateTotalQuantityBySkuCode(String skuCode) {
        // Use the InventoryRepository to fetch the inventories with the specified SKU code
        // and then calculate the sum of quantities.
        Integer totalQuantity = inventoryRepository.sumQuantityBySkuCode(skuCode);
        if (totalQuantity == null) {
            return 0; // Return 0 if no records are found.
        }
        return totalQuantity;
    }
    public ProductQuantityDetailsResponse getProductQuantityDetails(String skuCode) {
        Product product = productService.getProduct(skuCode);
        Integer totalQuantity = calculateTotalQuantityBySkuCode(skuCode);
        ProductQuantityDetailsResponse productQuantityDetailsResponse = ProductQuantityDetailsResponse.builder()
                .skuCode(product.getSkuCode())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .productPrice(product.getProductPrice())
                .productCategory(product.getProductCategory().toString())
                .productImageUrl(product.getProductImageUrl())
                .productAddedDate(product.getProductAddedDate().toString())
                .inventoryStatus(product.getInventoryStatus().toString())
                .quantity(totalQuantity)
                .build();
        return productQuantityDetailsResponse;
    }
    public boolean areItemsInStock(OrderRequestDTO orderRequest) {
        for (OrderItemDTO orderItem : orderRequest.getOrderItems()) {
            String skuCode = orderItem.getSkuCode();
            int orderedQuantity = orderItem.getOrderedQuantity();
            int availableQuantity = calculateTotalQuantityBySkuCode(skuCode);
            if (orderedQuantity > availableQuantity) {
                return false;
            }
        }
        return true;
    }
    public void processOrderV1(OrderRequestDTO orderRequest) {
        for (OrderItemDTO orderItem : orderRequest.getOrderItems()) {
            String skuCode = orderItem.getSkuCode();
            int orderedQuantity = orderItem.getOrderedQuantity();

            List<Inventory> inventories = inventoryRepository.findInventoriesBySkuCodeOrderByExpiryDate(skuCode);

            for (Inventory inventory : inventories) {
                int availableQuantity = inventory.getQuantity();

                if (orderedQuantity > 0) {
                    int quantityToDeduct = Math.min(orderedQuantity, availableQuantity);

                    // Deduct the quantity from the inventory
                    inventory.setQuantity(availableQuantity - quantityToDeduct);
                    updateInventory(inventory);

                    orderedQuantity -= quantityToDeduct;
                } else {
                    break; // No need to check further inventories
                }
            }
        }
    }


//    public void processOrder(OrderRequestDTO orderRequest) {
//        CartDTO cartDTO = createCartFromOrder(orderRequest);
//        cartService.saveCart(cartDTO);
//        deductOrderedQuantitiesFromInventory(orderRequest);
//    }

//    private CartDTO createCartFromOrder(OrderRequestDTO orderRequest) {
//        // Create a CartDTO and CartItemDTOs from the order request
//        CartDTO cartDTO = new CartDTO();
//        cartDTO.setId(orderRequest.getOrderId());
//        cartDTO.setWarehouseKeeperID(orderRequest.getWarehouseKeeperID());
//        cartDTO.setDeliveryManId(orderRequest.getDeliveryManId());
//
//        List<CartItemDTO> cartItems = orderRequest.getOrderItems().stream()
//                .map(orderItem -> {
//                    CartItemDTO cartItemDTO = new CartItemDTO();
//                    cartItemDTO.setSkuCode(orderItem.getSkuCode());
//                    cartItemDTO.setOrderedQuantity(orderItem.getOrderedQuantity());
//                    return cartItemDTO;
//                })
//                .collect(Collectors.toList());
//
//        cartDTO.setCartItems(cartItems);
//        return cartDTO;
//    }
//    private void deductOrderedQuantitiesFromInventory(OrderRequestDTO orderRequest) {
//        for (OrderItemDTO orderItem : orderRequest.getOrderItems()) {
//            String skuCode = orderItem.getSkuCode();
//            int orderedQuantity = orderItem.getOrderedQuantity();
//
//            List<Inventory> inventories = inventoryRepository.findInventoriesBySkuCodeOrderByExpiryDate(skuCode);
//
//            for (Inventory inventory : inventories) {
//                int availableQuantity = inventory.getQuantity();
//
//                if (orderedQuantity > 0) {
//                    int quantityToDeduct = Math.min(orderedQuantity, availableQuantity);
//
//                    // Deduct the quantity from the inventory
//                    inventory.setQuantity(availableQuantity - quantityToDeduct);
//                    updateInventory(inventory);
//
//                    orderedQuantity -= quantityToDeduct;
//                } else {
//                    break;
//                }
//            }
//        }
//    }




}
