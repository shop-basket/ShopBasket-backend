package com.shopbasket.orderservice.controller;

import com.shopbasket.orderservice.feign.NotificationInterface;
import com.shopbasket.orderservice.feign.ProductInterface;
import com.shopbasket.orderservice.model.*;
import com.shopbasket.orderservice.service.OrderService;

import com.shopbasket.orderservice.service.OrderedItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ShopBasket/api/order-service")
@CrossOrigin(allowedHeaders = "*" ,origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final OrderedItemService orderedItemService;
    NotificationInterface notificationInterface;
    ProductInterface productInterface;

    @Autowired
    public OrderController(OrderService orderService, OrderedItemService orderedItemService) {
        this.orderService = orderService;
        this.orderedItemService = orderedItemService;
    }

    // Retrieve delivery order details by order ID
    @GetMapping("/getDeliveryOrder/{oid}")
    public ResponseEntity<DeliveryOrderDTO> getDeliveryOrderByOid(@PathVariable Long oid){
        Order order = orderService.getOrderByOid(oid);
        DeliveryOrderDTO deliveryOrder = new DeliveryOrderDTO(order.getOid(), order.getTotalAmount(), order.getDeliveryAddress(), order.getDeliveryDate(), order.getCid(), order.getOmid(), order.getWkid(), order.getDpid(), order.getCshrid());
        return ResponseEntity.ok(deliveryOrder);
    }

    // Create a new order
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        Order createdOrder = orderService.createOrder(order);
        if (createdOrder == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // Add items to an existing order
    @PutMapping("/addItem/{oid}")
    public ResponseEntity<Order> addItems(@PathVariable Long oid, @RequestBody OrderedItem item){
        Order updatedOrder = orderService.addItem(oid,item);
        orderedItemService.addItem(oid,item);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedOrder);
    }

    // Set Order Manager for an order and notify them
    @PutMapping("/setOM/{oid}")
    public ResponseEntity<Order> setOrderManager(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setOM(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        // Notify the Order Manager
//        NotificationRequest notificationRequest = new NotificationRequest(oid, eid, "You have a new order. Do you accept?");
//        notificationInterface.sendNotification(notificationRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    // Set Warehouse Keeper for an order and notify them
    @PutMapping("/setWK/{oid}")
    public ResponseEntity<Order> setWarehouseKeeper(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setWK(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        // Notify the Warehouse Keeper
//        NotificationRequest notificationRequest = new NotificationRequest(oid, eid, "You have a new order. Do you accept?");
//        notificationInterface.sendNotification(notificationRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    // Set Delivery Person for an order and notify them
    @PutMapping("/setDP/{oid}")
    public ResponseEntity<Order> setDeliveryPerson(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setDP(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        // Notify the Delivery Person
//        NotificationRequest notificationRequest = new NotificationRequest(oid, eid, "You have a new order. Do you accept?");
//        notificationInterface.sendNotification(notificationRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    // Set Cashier for an order and notify them
    @PutMapping("/setCshr/{oid}")
    public ResponseEntity<Order> setCashier(@PathVariable Long oid, @RequestParam Long eid) {
        if (oid == null || eid == null) {
            return ResponseEntity.badRequest().build();
        }
        Order updatedOrder = orderService.setCshr(oid, eid);
        if (updatedOrder == null) {
            return ResponseEntity.notFound().build();
        }
        // Notify the Cashier
//        NotificationRequest notificationRequest = new NotificationRequest(oid, eid, "You have a new order. Do you accept?");
//        notificationInterface.sendNotification(notificationRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    // Set status for an order
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
                if (status.toString().equals("SHIPPED")){
//                    productInterface.updateStock();
                }
                return ResponseEntity.ok(updatedOrder);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    // Show items in the cart for a customer
    @GetMapping("/showCart/{cid}")
    public ResponseEntity<List<Order>> showCart(@PathVariable Long cid){
        List<Order> orders = orderService.getOrderByCid(cid);
        if (orders == null) {
            return ResponseEntity.notFound().build();
        }
        List<Order> myCart = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.PAID)
                .collect(Collectors.toList());
        return ResponseEntity.ok(myCart);
    }

    // Retrieve order details by order ID
    @GetMapping("/getOrder/{oid}")
    public ResponseEntity<Order> getOrderByOid(@PathVariable Long oid){
        Order order = orderService.getOrderByOid(oid);
        return ResponseEntity.ok(order);
    }


    // Retrieve orders by customer ID
    @GetMapping("/getOrderByCid/{uid}")
    public ResponseEntity<List<Order>> getOrderByCid(@PathVariable Long uid){
        List<Order> order = orderService.getOrderByCid(uid);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}
