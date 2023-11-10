package com.shopbasket.orderservice.feign;

import com.shopbasket.orderservice.model.OrderedItem;
import com.shopbasket.orderservice.model.ProductWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("Inventory-Service")
public interface ProductInterface {

    @GetMapping("product/{pid}")
    ResponseEntity<ProductWrapper> getProductByPid (@PathVariable Long pid);

    @GetMapping("product/checkAvailability")
    ResponseEntity<Boolean> checkAvailability (@RequestBody List<Long> pid);

}