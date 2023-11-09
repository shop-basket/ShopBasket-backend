package com.ShopBasket.deliveryservice.controller;

import com.ShopBasket.deliveryservice.model.Delivery;
import com.ShopBasket.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/get-all-deliveries")
    @ResponseStatus(HttpStatus.OK)
    public List<Delivery> getAllDeliveries() {
        return deliveryService.getAllDeliveries();
    }

    @GetMapping("/get-delivery/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getDeliveryById(@PathVariable Long id) {
        try {
            Delivery delivery = deliveryService.getDeliveryById(id);
            return ResponseEntity.ok(delivery);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


    // Create a new delivery
    @PostMapping("/create-delivery")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createDelivery(@RequestBody Delivery delivery) {
        try {
            if (delivery == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Delivery createdDelivery = deliveryService.createDelivery(delivery);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDelivery);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Update a delivery
    @PutMapping("/update-delivery/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateDelivery(@PathVariable Long id, @RequestBody Delivery delivery) {
        try {
            if (delivery == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Delivery updatedDelivery = deliveryService.updateDelivery(id, delivery);
            return ResponseEntity.status(HttpStatus.OK).body(updatedDelivery);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Delivery not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

}
