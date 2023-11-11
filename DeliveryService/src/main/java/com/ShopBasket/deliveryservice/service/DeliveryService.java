package com.ShopBasket.deliveryservice.service;


import com.ShopBasket.deliveryservice.model.Delivery;
import com.ShopBasket.deliveryservice.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery createDelivery(Delivery delivery) {
        try {
            // Save the delivery
            return deliveryRepository.save(delivery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create the delivery: " + e.getMessage());
        }
    }

    public Delivery getDeliveryById(Long id) {
        return deliveryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Delivery not found with id: " + id));

    }

    public Delivery updateDelivery(Long id, Delivery delivery) {
        try {
            // Get the delivery by id
            Delivery existingDelivery = deliveryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Delivery not found with id: " + id));

            // Update the delivery
            existingDelivery.setCourierId(delivery.getCourierId());
            existingDelivery.setOrderManagerId(delivery.getOrderManagerId());
            existingDelivery.setOrderId(delivery.getOrderId());
            existingDelivery.setDate(delivery.getDate());
            existingDelivery.setStatus(delivery.getStatus());

            // Save the updated delivery
            return deliveryRepository.save(existingDelivery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update the delivery: " + e.getMessage());
        }
    }
}
