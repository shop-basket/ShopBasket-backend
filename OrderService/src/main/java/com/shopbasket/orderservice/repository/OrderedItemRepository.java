package com.shopbasket.orderservice.repository;

import com.shopbasket.orderservice.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedItemRepository extends JpaRepository<OrderedItem,Long> {
}
