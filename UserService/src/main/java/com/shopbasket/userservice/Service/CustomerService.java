package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Entities.ConfirmationEmailToken;
import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationEmailTokenService confirmationEmailTokenService;
    private final EmailSender emailSender;
    private final EmailService emailService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public AuthenticationResponse customerAuthenticate(AuthenticationRequest request) {
        UserDetails user = loadUserByUsername(request.getEmail());

        if(user.isEnabled()){
            if (user != null  && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                var jwtToken = jwtService.generateTokenForCustomer(request.getEmail());
                return AuthenticationResponse.builder().token(jwtToken).build();
            } else {
                return AuthenticationResponse.builder()
                        .message("Authentication failed. Please check your credentials.")
                        .build();
            }
        }else{
            Optional<Customer> customer = customerRepository.findByEmail(request.getEmail());
            //  generating token
            String token = UUID.randomUUID().toString();
            ConfirmationEmailToken confirmationEmailToken =  new ConfirmationEmailToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    customer.get().getId()
            );
            String fullName = customer.get().getFirstName() + " " + customer.get().getLastName();
            String link = "http://localhost:8080/ShopBasket/api/auth/confirm?token="+token;
            emailSender.send(customer.get().getEmail(), emailService.buildEmail(fullName, link));

            confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);
            return AuthenticationResponse.builder()
                    .message("Authentication failed. Please Verify your email")
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
            //  generating token
            String token = UUID.randomUUID().toString();
            ConfirmationEmailToken confirmationEmailToken =  new ConfirmationEmailToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    customer.getId()
            );
            String fullName = registerRequest.getFirstName() + " " + registerRequest.getLastName();
            String link = "http://localhost:8080/ShopBasket/api/customerAuth/confirm?token="+token;
            emailSender.send(registerRequest.getEmail(), emailService.buildEmail(fullName, link));

            confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);

            var jwtToken = jwtService.generateTokenForCustomer(customer.getEmail());

            return AuthenticationResponse.builder().token(jwtToken).message("Please verify your email").build();
        }else{
            //if email is not confirmed send the email again
            if(!fetchCustomer.get().isEnabled()){
                //  generating token
                String token = UUID.randomUUID().toString();
                ConfirmationEmailToken confirmationEmailToken =  new ConfirmationEmailToken(
                        token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        fetchCustomer.get().getId()
                );
                String fullName = registerRequest.getFirstName() + " " + registerRequest.getLastName();
                String link = "http://localhost:8080/ShopBasket/api/customerAuth/confirm?token="+token;
                emailSender.send(registerRequest.getEmail(), emailService.buildEmail(fullName, link));

                confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);

                var jwtToken = jwtService.generateTokenForCustomer(fetchCustomer.get().getEmail());

                return AuthenticationResponse.builder().token(jwtToken).build();
            }
            return AuthenticationResponse.builder()
                    .message("User with this email is already registered")
                    .build();
        }
    }
    @Transactional
    public String confirmToken(String token){
        ConfirmationEmailToken confirmationEmailToken = confirmationEmailTokenService.getToken(token)
                .orElseThrow(()-> new IllegalStateException("token not found"));
        if(confirmationEmailToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationEmailToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        confirmationEmailTokenService.setConfirmedAt(token);
        customerRepository.updateEnabled(confirmationEmailToken.getUserId());
        return "confirmed";
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
