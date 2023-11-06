package com.shopbasket.userservice.DTO;

import com.shopbasket.userservice.Repository.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreationResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Integer phoneNo;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String receivedJwtToken;
}
