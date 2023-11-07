package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Entities.Employee;
import com.shopbasket.userservice.Repository.EmployeeRepository;
import com.shopbasket.userservice.Repository.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeAuthService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    TEMPORARY
    public AuthenticationResponse register(RegisterRequest request) {
        var fetchEmployee = employeeRepository.findByEmail(request.getEmail());
        boolean notExistsEmployee = fetchEmployee.isEmpty();

    if(notExistsEmployee ){
            var employee =Employee.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.SystemAdmin)
                    .phoneNo(request.getPhoneNo())
                    .build();
        System.out.println("Employee:::"+employee);
            employeeRepository.save(employee);
            var jwtToken = jwtService.generateToken(employee);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }else{
            return AuthenticationResponse.builder()
                    .message("User with this email is already registered")
                    .build();
        }
    }

    @Transactional
    public AuthenticationResponse empAuthenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken =  jwtService.generateToken(employee);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        System.out.println("changePassword Service"+changePasswordRequest);
        // Retrieve the current user's details
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(changePasswordRequest.getEmail());

            if (employeeOptional.isEmpty()) {
                return new MessageResponse("User not found");
            }

            Employee employee = employeeOptional.get();

            // Verify that the provided current password matches the stored current password
            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), employee.getPassword())) {
                return new MessageResponse("Incorrect current password");
            }

            // Update the password with the new one
            employee.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            employeeRepository.save(employee);

            return new MessageResponse("Password changed successfully");
    }
}
