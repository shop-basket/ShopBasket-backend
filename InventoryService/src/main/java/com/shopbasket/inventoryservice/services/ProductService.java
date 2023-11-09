package com.shopbasket.inventoryservice.services;

import com.shopbasket.inventoryservice.Exceptions.ProductNotFoundException;
import com.shopbasket.inventoryservice.Exceptions.ProductUpdateException;
import com.shopbasket.inventoryservice.dto.ProductRequest;
import com.shopbasket.inventoryservice.entities.Product;
import com.shopbasket.inventoryservice.enums.ProductCategory;
import com.shopbasket.inventoryservice.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository ;
    @Autowired

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }




    public Product addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .productDescription(productRequest.getProductDescription())
                .productPrice(productRequest.getProductPrice())
                .skuCode(productRequest.getSkuCode())
                .productImageUrl(productRequest.getProductImageUrl())
                .productAddedDate(productRequest.getProductAddedDate())
                .productCategory(productRequest.getProductCategory())
                .build();

        return productRepository.save(product);
     }
     // Bussiness Logic for Fetching products by SkuCode
        public Product getProduct(String skuCode) {
            return productRepository.findById(skuCode).orElse(null);
        }

        // Bussiness Logic for Deleting a Product
        public void deleteProduct(String skuCode) {
            productRepository.deleteById(skuCode);
        }

 //Fetching the List of Product Categories
    public List<ProductCategory> getProductCategories() {
        return List.of(ProductCategory.values());
    }
    // Bussiness Logic for Updating a Product

    public ProductRequest updateProduct(String skuCode, ProductRequest productRequest) {
        try {
            Product product = productRepository.findById(skuCode)
                    .orElseThrow(() -> new ProductNotFoundException("Product with SKU code " + skuCode + " not found"));
            product.setProductName(productRequest.getProductName());
            product.setProductDescription(productRequest.getProductDescription());
            product.setProductPrice(productRequest.getProductPrice());
            product.setProductImageUrl(productRequest.getProductImageUrl());
            product.setProductAddedDate(productRequest.getProductAddedDate());
            product.setProductCategory(productRequest.getProductCategory());

            productRepository.save(product);
            return productRequest;
        } catch (ProductNotFoundException e) {
            log.error("Product not found: SKU code = {}", skuCode, e);

            throw e;
        } catch (Exception e) {
            log.error("Failed to update product: SKU code = {}", skuCode, e);
            throw new ProductUpdateException("Failed to update product", e);
        }
    }


    //Fetching All Products and View Details
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //Category wise Filter Products
    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

}
