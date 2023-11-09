package com.shopbasket.userservice.Repository;

import com.shopbasket.userservice.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Employee e " +
            "SET e.enabled = true " +
            "WHERE e.id = ?1")
    int updateEnabled(Integer id);
}
