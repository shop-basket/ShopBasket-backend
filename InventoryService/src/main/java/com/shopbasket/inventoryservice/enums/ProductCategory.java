package com.shopbasket.inventoryservice.enums;

import lombok.Data;




public enum ProductCategory {
    DRY_FOOD("Dry Food"),
    VEGETABLE("Vegetable"),
    DAIRY_ITEMS("Dairy Items"),
    MEAT("Meat"),
    FROZEN_FOODS("Frozen Foods"),
    COSMETICS("Cosmetics"),
    CLEANING_SUPPLIES("Cleaning Supplies"),
    BABY_PRODUCTS("Baby Products"),

    HEALTH_SUPPLEMENTS("Health Supplements"),
    BEVERAGES("Beverages"),
    PET_SUPPLIES("Pet Supplies"),
    OTHER("Other");
    private final String label;

    ProductCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
