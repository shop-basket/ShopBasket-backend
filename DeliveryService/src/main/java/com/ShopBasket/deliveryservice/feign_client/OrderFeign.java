package com.ShopBasket.deliveryservice.feign_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderFeign {
//    @GetMapping("/getOrder/{oid}")
//    ResponseEntity<Order> getOrderByOid(@PathVariable Long oid);
}


/*

@GetMapping("/getOrder/{oid}")
    public ResponseEntity<Order> getOrderByOid(@PathVariable Long oid){
        Order order = orderService.getOrderByOid(oid);
        return ResponseEntity.ok(order);
}
 */