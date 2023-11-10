package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Entities.ConfirmationEmailToken;
import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Entities.Employee;
import com.shopbasket.userservice.Repository.ConfirmationEmailTokenRepository;
import com.shopbasket.userservice.Repository.EmployeeRepository;
import com.shopbasket.userservice.Repository.Role;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeAuthService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;
    private final EmailSender emailSender;
    private final ConfirmationEmailTokenService confirmationEmailTokenService;
    private ConfirmationEmailTokenRepository confirmationEmailTokenRepository;

    //    TEMPORARY For adding System Admin
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

            employeeRepository.save(employee);
            //  generating token
            String token = UUID.randomUUID().toString();
            ConfirmationEmailToken confirmationEmailToken =  new ConfirmationEmailToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    employee.getId(),
                    employee.getEmail()
            );
            String fullName = employee.getFirstName() + " " + employee.getLastName();
            String link = "http://localhost:8080/ShopBasket/api/auth/confirm?token="+token;
            emailSender.send(employee.getEmail(), emailService.buildEmail(fullName, link));

            confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);

            var jwtToken = jwtService.generateToken(employee);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }else{
            return AuthenticationResponse.builder()
                    .message("User with this email is already registered")
                    .build();
        }
    }


    public AuthenticationResponse empAuthenticate(AuthenticationRequest request) {
        try {
            var employee = employeeRepository.findByEmail(request.getEmail())
                    .orElseThrow(()-> new UsernameNotFoundException(
                            String.format("User with email %s not found",request.getEmail())
                    ));
            if(employee.isEnabled()){
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
                var jwtToken = jwtService.generateToken(employee);
                return AuthenticationResponse.builder().token(jwtToken).build();
            }else{
                //  generating token
                String token = UUID.randomUUID().toString();
                ConfirmationEmailToken confirmationEmailToken =  new ConfirmationEmailToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        employee.getId(),
                        employee.getEmail()
                );
                String fullName = employee.getFirstName() + " " + employee.getLastName();
                String link = "http://localhost:8080/ShopBasket/api/auth/confirm?token="+token;
                emailSender.send(employee.getEmail(), emailService.buildEmail(fullName, link));
                confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);
            }
            return AuthenticationResponse.builder().message("Please verify your email").build();

        }catch (AuthenticationException e){
            return AuthenticationResponse.builder()
                    .message("Authentication failed. Please check your credentials.")
                    .build();
        }
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

    @Transactional
    public String confirmToken(String token){
        try {
            ConfirmationEmailToken confirmationEmailToken = confirmationEmailTokenService.getToken(token)
                    .orElseThrow(() -> new IllegalStateException("token not found"));
            if (confirmationEmailToken.getConfirmedAt() != null) {
                throw new IllegalStateException("email already confirmed");
            }
            LocalDateTime expiredAt = confirmationEmailToken.getExpiresAt();

            if (expiredAt.isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("token expired");
            }
            confirmationEmailTokenService.setConfirmedAt(token);
            employeeRepository.updateEnabled(confirmationEmailToken.getUserId());
            return "confirmed";
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public String updateProfile(Integer id, EmployeeUpdateProfile employeeUpdateProfile) {
        Optional<Employee> employee = employeeRepository.findById(id);
        Employee updateEmployee = employee.get();

        if(employee.isPresent()){
            if(employee.get().isEnabled()){
                if(StringUtils.isNotEmpty(employeeUpdateProfile.getFirstName())){
                    updateEmployee.setFirstName(employeeUpdateProfile.getFirstName());
                }
                if(StringUtils.isNotEmpty(employeeUpdateProfile.getLastName())){
                    updateEmployee.setLastName(employeeUpdateProfile.getLastName());
                }
                if(employeeUpdateProfile.getPhoneNo() != null){
                    updateEmployee.setPhoneNo(employeeUpdateProfile.getPhoneNo());
                }
                if(StringUtils.isNotEmpty(employeeUpdateProfile.getProfileURL())){
                    updateEmployee.setProfileURL(employeeUpdateProfile.getProfileURL());
                }
                employeeRepository.save(updateEmployee);
                return "Profile updated successfully";
            }else {
                return "Please verify your email";
            }
        }
        return "Employee not found";
    }
    @Transactional
    public String deleteAcc(Integer id, String password) {
        try {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);
            String role = String.valueOf(Role.SystemAdmin);
            if(employeeOptional.get().getRole().equals(role)) {
                if (employeeOptional.isEmpty()) {
                    throw new IllegalStateException("User not found");
                }

                Employee employee = employeeOptional.get();

                if (passwordEncoder.matches(password, employee.getPassword())) {
                    employeeRepository.deleteById(id);
//                    Optional<ConfirmationEmailToken> confirmationEmailToken = confirmationEmailTokenRepository.findByUserId(id);
//                    if (confirmationEmailToken.isPresent()) {
//                        confirmationEmailTokenRepository.deleteByEmail(employee.getEmail());
//                    }
                    return "Deleted successfully";
                } else {
                    return "Password is incorrect";
                }
            }else{
                return "System Admin account can't be deleted!";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}