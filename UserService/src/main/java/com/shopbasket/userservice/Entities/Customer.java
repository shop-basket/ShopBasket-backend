package com.shopbasket.userservice.Entities;

import jakarta.persistence.*;
import lombok.*;

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
    private Integer zipCode;
    @Builder
    public Customer(String firstName, String lastName, String email, String password, Integer phoneNo,
                    String profileURL,String street, String city,String province,Integer zipCode) {
        super(firstName, lastName, email, password, phoneNo, profileURL);
        this.street=street;
        this.city=city;
        this.province =province;
        this.zipCode = zipCode;
    }
    public Customer() {
        super();
    }
}
