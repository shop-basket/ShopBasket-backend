package com.shopbasket.inventoryservice.controllers;

import com.shopbasket.inventoryservice.dto.ProductRequest;
import com.shopbasket.inventoryservice.enums.ProductCategory;
import com.shopbasket.inventoryservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ShopBasket/api/products")
public class ProductController {
    private final ProductService productService;
    @Autowired

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Rest api for adding a product
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.ok("Product with SKU code " + productRequest.getSkuCode() + "and" + productRequest.getProductName() + " has been successfully added.");
    }
    //Rest API for Fetching the List of Product Categories
    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductCategory> getProductCategories() {
        return productService.getProductCategories();
    }
    @PutMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ProductRequest updateProduct(@PathVariable String skuCode, @RequestBody ProductRequest productRequest) {
        return productService.updateProduct(skuCode, productRequest);
    }
    @DeleteMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteProduct(@PathVariable String skuCode) {

        String productName = productService.getProduct(skuCode).getProductName();
        productService.deleteProduct(skuCode);
        return ResponseEntity.ok("Product with SKU code " + productName + " has been successfully deleted.");
    }


}
