package com.shopbasket.userservice.Entities;

import com.shopbasket.userservice.Repository.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Entity
@Table
public class Employee extends Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Employee(String firstName, String lastName, String email, String password, Integer phoneNo, String profileURL, Role role) {
        super(firstName, lastName, email, password, phoneNo, profileURL);
        this.role=role;
    }

    public Employee() {
        super();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
