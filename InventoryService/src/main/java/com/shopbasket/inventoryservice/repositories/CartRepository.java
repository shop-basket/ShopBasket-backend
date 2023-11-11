package com.shopbasket.inventoryservice.repositories;

import com.shopbasket.inventoryservice.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,String> {

}
