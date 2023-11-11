package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import com.shopbasket.userservice.Entities.Employee;
import com.shopbasket.userservice.Repository.EmployeeRepository;
import com.shopbasket.userservice.Repository.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SystemAdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserValidationService userValidationService;

    public Employee  saveEmployee(EmployeeCreationResponse employeeCreationResponse) {
        Employee employeeDetails = null;
        final String defaultPassword = "ShopBasket*123";
        boolean notExists = employeeRepository.findByEmail(employeeCreationResponse.getEmail()).isEmpty();
        var extractRole=jwtService.extractRole(employeeCreationResponse.getReceivedJwtToken());
        boolean validCredentials = userValidationService.employeeCredentialValidation(employeeCreationResponse);

        if (extractRole.equals("SystemAdmin")) {
           if(notExists && validCredentials) {
               employeeDetails = Employee.builder()
                       .email(employeeCreationResponse.getEmail())
                       .firstName(employeeCreationResponse.getFirstName())
                       .phoneNo(employeeCreationResponse.getPhoneNo())
                       .lastName(employeeCreationResponse.getLastName())
                       .password(passwordEncoder.encode(defaultPassword))
                       .role(employeeCreationResponse.getRole())
                       .build();
               employeeRepository.save(employeeDetails);
           }
        }
        return employeeDetails;
    }
}
