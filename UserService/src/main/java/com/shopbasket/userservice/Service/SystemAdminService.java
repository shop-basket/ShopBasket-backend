package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import com.shopbasket.userservice.Entities.ConfirmationEmailToken;
import com.shopbasket.userservice.Entities.Employee;
import com.shopbasket.userservice.Repository.EmployeeRepository;
import com.shopbasket.userservice.Repository.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ConfirmationEmailTokenService confirmationEmailTokenService;
    @Autowired
    private EmailService emailService;

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
                //  generating token
                String token = UUID.randomUUID().toString();
                ConfirmationEmailToken confirmationEmailToken =  new ConfirmationEmailToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        employeeDetails.getId(),
                        employeeDetails.getEmail()
                );
                String fullName = employeeCreationResponse.getFirstName() + " " + employeeCreationResponse.getLastName();
                String link = "http://localhost:8089/ShopBasket/api/auth/confirm?token="+token;
                emailSender.send(employeeDetails.getEmail(),emailService.buildEmail(fullName,link));

                confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);
            }
        }
        return employeeDetails;
    }
    public String modifyUsers(Integer id, String token, Role newRole) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        if (employeeOptional.isPresent()) {
            var extractRole = jwtService.extractRole(token);

            if (extractRole.equals("SystemAdmin")) {
                Employee employee = employeeOptional.get();
                employeeRepository.updateRole(id,newRole);
                employeeRepository.save(employee);
                return "Modified successfully";
            } else {
                return "Permission denied. Only SystemAdmin can modify roles.";
            }
        } else {
            return "Employee not found";
        }
    }
}