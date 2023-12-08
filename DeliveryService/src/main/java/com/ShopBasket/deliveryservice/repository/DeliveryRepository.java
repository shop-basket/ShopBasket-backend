package com.ShopBasket.deliveryservice.repository;

import com.ShopBasket.deliveryservice.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
