package com.shopbasket.orderservice.controller;

import com.shopbasket.orderservice.model.DeliveryOrderDTO;
import com.shopbasket.orderservice.model.Order;
import com.shopbasket.orderservice.model.OrderStatus;
import com.shopbasket.orderservice.model.OrderedItem;
import com.shopbasket.orderservice.service.OrderService;

import com.shopbasket.orderservice.service.OrderedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@CrossOrigin(allowedHeaders = "*" ,origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final OrderedItemService orderedItemService;

    @Autowired
    public OrderController(OrderService orderService, OrderedItemService orderedItemService) {
        this.orderService = orderService;
        this.orderedItemService = orderedItemService;
    }

    @GetMapping("/getDeliveryOrder/{oid}")
    public ResponseEntity<DeliveryOrderDTO> getDeliveryOrderByOid(@PathVariable Long oid){
        Order order = orderService.getOrderByOid(oid);
        DeliveryOrderDTO deliveryOrder = new DeliveryOrderDTO(order.getOid(), order.getTotalAmount(), order.getDeliveryAddress(), order.getDeliveryDate(), order.getCid(), order.getOmid(), order.getWkid(), order.getDpid(), order.getCshrid());
        return ResponseEntity.ok(deliveryOrder);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        Order createdOrder = orderService.createOrder(order);
        if (createdOrder == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/addItem/{oid}")
    public ResponseEntity<Order> addItems(@PathVariable Long oid, @RequestBody OrderedItem item){
        Order updatedOrder = orderService.addItem(oid,item);
        orderedItemService.addItem(oid,item);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<List<Order>> getOrderByCid(@PathVariable Long uid){
        List<Order> order = orderService.getOrderByCid(uid);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping("/setOM/{oid}")
    public ResponseEntity<Order> setOrderManager(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setOM(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/setWK/{oid}")
    public ResponseEntity<Order> setWarehouseKeeper(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setWK(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/setDP/{oid}")
    public ResponseEntity<Order> setDeliveryPerson(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setDP(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/setCshr/{oid}")
    public ResponseEntity<Order> setCashier(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setCshr(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/setStatus/{oid}")
    public ResponseEntity<Order> setStatus(@PathVariable Long oid, @RequestBody Map<String, String> statusMap) {
        String statusValue = statusMap.get("status");
        if (statusValue != null) {
            try {
                OrderStatus status = OrderStatus.valueOf(statusValue);
                Order updatedOrder = orderService.setStatus(oid, status);
                if (updatedOrder == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(updatedOrder);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/showCart/{cid}")
    public ResponseEntity<List<Order>> showCart(@PathVariable Long cid){
        List<Order> orders = orderService.getOrderByCid(cid);
        if (orders == null) {
            return ResponseEntity.notFound().build();
        }
        List<Order> myCart = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.DELIVERED)
                .collect(Collectors.toList());
        return ResponseEntity.ok(myCart);
    }
}
