package com.shopbasket.orderservice.feign;

import com.shopbasket.orderservice.model.ProductWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("Inventory-Service")
public interface ProductInterface {

    @GetMapping("product/{pid}")
    ResponseEntity<ProductWrapper> getProductByPid (@PathVariable Long pid);

}