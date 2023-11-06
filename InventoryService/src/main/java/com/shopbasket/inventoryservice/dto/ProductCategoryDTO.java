package com.shopbasket.inventoryservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDTO {
    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must be less than or equal to 50 characters")
    private String categoryName;
    @Size(max = 255, message = "Category description must be less than or equal to 255 characters")
    private String categoryDescription;
}
