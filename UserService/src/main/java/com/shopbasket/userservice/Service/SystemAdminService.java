package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.Config.JwtService;
import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import com.shopbasket.userservice.Entities.Employee;
import com.shopbasket.userservice.Repository.EmployeeRepository;
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

    public Employee  saveEmployee(EmployeeCreationResponse employeeCreationResponse) {
        Employee employeeDetails = null;
        final String defaultPassword = "1234";
        boolean notExists = employeeRepository.findByEmail(employeeCreationResponse.getEmail()).isEmpty();
        var extractRole=jwtService.extractRole(employeeCreationResponse.getReceivedJwtToken());

        if (extractRole.equals("SystemAdmin")) {
           if(notExists) {
               employeeDetails = Employee.builder()
                       .email(employeeCreationResponse.getEmail())
                       .firstName(employeeCreationResponse.getFirstName())
                       .role(employeeCreationResponse.getRole())
                       .phoneNo(employeeCreationResponse.getPhoneNo())
                       .lastName(employeeCreationResponse.getLastName())
                       .password(passwordEncoder.encode(defaultPassword))
                       .build();
               employeeRepository.save(employeeDetails);
           }
        }
//        return employeeDetails;
        return employeeDetails;
    }
}
