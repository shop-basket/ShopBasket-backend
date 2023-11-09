package com.shopbasket.userservice.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table
public class Customer extends Users{
    private String street;
    private String city;
    private String province;
    private Integer zipCode;
    @Builder
    public Customer(Integer id,String firstName, String lastName, String email, String password, Integer phoneNo,
                    String profileURL,boolean locked, boolean enabled,String street, String city,String province,Integer zipCode) {
        super(id, firstName, lastName, email, password, phoneNo, profileURL, enabled,locked);
        this.street=street;
        this.city=city;
        this.province =province;
        this.zipCode = zipCode;
    }
    public Customer() {
        super();
    }
}
