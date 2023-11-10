package com.shopbasket.userservice.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateProfile {
    private String firstName;
    private String lastName;
    @Column(length = 15)
    private Integer phoneNo;
    private String profileURL;
    private String street;
    private String city;
    private String province;
    private Integer zipCode;
}
