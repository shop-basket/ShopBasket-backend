package com.shopbasket.orderservice.feign;

import com.shopbasket.orderservice.model.OrderRequestDTO;
import com.shopbasket.orderservice.model.ProductWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("Inventory-Service")
public interface ProductInterface {

    @GetMapping("product/{skuCode}")
    ResponseEntity<ProductWrapper> getProductBySkuCode (@PathVariable String skuCode);

    @PostMapping("/check-order")
    ResponseEntity<String> prepareAndCheckOrder(@RequestBody OrderRequestDTO orderRequestDTO);

}

