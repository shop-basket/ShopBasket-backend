package com.shopbasket.userservice.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateProfile {
    private String firstName;
    private String lastName;
    @Column(length = 15)
    private Integer phoneNo;
    private String profileURL;
}
