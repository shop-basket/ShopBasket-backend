package com.shopbasket.orderservice.repository;

import com.shopbasket.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o WHERE o.cid = :cid")
    List<Order> findOrdersByCustomerId(@Param("cid") Long cid);
}
