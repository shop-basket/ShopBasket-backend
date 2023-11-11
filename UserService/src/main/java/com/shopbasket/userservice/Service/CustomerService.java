package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    @Transactional
    public AuthenticationResponse customerAuthenticate(AuthenticationRequest request) {
        UserDetails user = loadUserByUsername(request.getEmail());

        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            var jwtToken = jwtService.generateTokenForCustomer((Customer) user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } else {
            return AuthenticationResponse.builder()
                    .message("Authentication failed. Please check your credentials.")
                    .build();
        }
    }
    @Transactional
    public AuthenticationResponse customerRegister(CustomerRegisterRequest registerRequest) {
        var fetchCustomer = customerRepository.findByEmail(registerRequest.getEmail());
        boolean notExistsCustomer = fetchCustomer.isEmpty();
        boolean validCredentials = userValidationService.customerCredentialValidation(registerRequest);

        if(notExistsCustomer && validCredentials){
            var  customer = Customer.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .phoneNo(registerRequest.getPhoneNo())
                    .street(registerRequest.getStreet())
                    .city(registerRequest.getCity())
                    .province(registerRequest.getProvince())
                    .zipCode(registerRequest.getZipCode())
                    .build();
            customerRepository.save(customer);
            var jwtToken = jwtService.generateTokenForCustomer(customer);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }else{
            return AuthenticationResponse.builder()
                    .message("User with this email is already registered")
                    .build();
        }
    }
    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        // Retrieve the current user's details
        Optional<Customer> customerOptional = customerRepository.findByEmail(changePasswordRequest.getEmail());

        if (customerOptional.isEmpty()) {
            return new MessageResponse("User not found");
        }

        Customer customer = customerOptional.get();

        // Verify that the provided current password matches the stored current password
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), customer.getPassword())) {
            return new MessageResponse("Incorrect current password");
        }

        // Update the password with the new one
        customer.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        customerRepository.save(customer);

        return new MessageResponse("Password changed successfully");
    }
}
