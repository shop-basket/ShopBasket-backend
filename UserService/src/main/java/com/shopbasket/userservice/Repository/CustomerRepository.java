package com.shopbasket.userservice.Repository;

import com.shopbasket.userservice.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Customer c " +
            "SET c.enabled = true " +
            "WHERE c.id = ?1")
    int updateEnabled(Integer id);

    Optional<Customer> deleteByEmail(String email);
}
