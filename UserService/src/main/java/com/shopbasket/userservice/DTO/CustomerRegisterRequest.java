package com.shopbasket.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer phoneNo;
    private String street;
    private String city;
    private String province;
    private Integer ZIPCode;
}
