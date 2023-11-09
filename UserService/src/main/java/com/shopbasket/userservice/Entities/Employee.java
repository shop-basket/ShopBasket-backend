package com.shopbasket.userservice.Entities;

import com.shopbasket.userservice.Repository.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Entity
@Table
public class Employee extends Users {
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Employee(Integer id,String firstName, String lastName, String email, String password, Integer phoneNo,
                    String profileURL, boolean locked, boolean enabled,Role role) {
        super(id,firstName, lastName, email, password, phoneNo, profileURL, locked,enabled);
        this.role=role;
    }

    public Employee() {
        super();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.name());
        return Collections.singleton(simpleGrantedAuthority);
    }
}
