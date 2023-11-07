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
public class Customer extends Users{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String street;
    private String city;
    private String province;
    private Integer ZIPCode;
    @Builder
    public Customer(String firstName, String lastName, String email, String password, Integer phoneNo, String profileURL,String street, String city,String province,Integer ZIPCode) {
        super(firstName, lastName, email, password, phoneNo, profileURL);
        this.street=street;
        this.city=city;
        this.province =province;
        this.ZIPCode = ZIPCode;
    }
    public Customer() {
        super();
    }
}
