package com.shopbasket.inventoryservice.enums;

public enum InventoryStatus {
    IN_STOCK("In Stock"),
    OUT_OF_STOCK("Out of Stock");


    private final String label;

    InventoryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
