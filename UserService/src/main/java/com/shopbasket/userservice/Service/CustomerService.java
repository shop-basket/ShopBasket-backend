package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Entities.ConfirmationEmailToken;
import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Repository.ConfirmationEmailTokenRepository;
import com.shopbasket.userservice.Repository.CustomerRepository;
import io.micrometer.common.util.StringUtils;
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
    @Autowired
    private final EmailService emailService;
    private final ConfirmationEmailTokenRepository confirmationEmailTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public AuthenticationResponse customerAuthenticate(AuthenticationRequest request) {
        UserDetails user = loadUserByUsername(request.getEmail());
        if(user.equals(null)){
            return AuthenticationResponse.builder().message("User is not exists. Please check your email address").build();
        }

        if(user.isEnabled()){
            if (user != null  && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                Optional<Customer> customer = customerRepository.findByEmail(request.getEmail());
                var jwtToken = jwtService.generateTokenForCustomer(customer.get());
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
                    customer.get().getId(),
                    customer.get().getEmail()
            );
            String fullName = customer.get().getFirstName() + " " + customer.get().getLastName();
            String link = "http://localhost:8089/ShopBasket/api/customerAuth/confirm?token="+token;
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
                    customer.getId(),
                    customer.getEmail()
            );
            String fullName = registerRequest.getFirstName() + " " + registerRequest.getLastName();
            String link = "http://localhost:8089/ShopBasket/api/customerAuth/confirm?token="+token;
            emailSender.send(registerRequest.getEmail(), emailService.buildEmail(fullName, link));

            confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);

            var jwtToken = jwtService.generateTokenForCustomer(customer);

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
                        fetchCustomer.get().getId(),
                        fetchCustomer.get().getEmail()
                );
                String fullName = registerRequest.getFirstName() + " " + registerRequest.getLastName();
                String link = "http://localhost:8089/ShopBasket/api/customerAuth/confirm?token="+token;
                emailSender.send(registerRequest.getEmail(), emailService.buildEmail(fullName, link));

                confirmationEmailTokenService.saveConfirmationToken(confirmationEmailToken);

                var jwtToken = jwtService.
                        generateTokenForCustomer(fetchCustomer.get());

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

    public String updateProfile(Integer id, CustomerUpdateProfile customerUpdateProfile) {
        Optional<Customer> customer = customerRepository.findById(id);
        Customer updateCustomer = customer.get();

        if(customer.isPresent() ){
            if(customer.get().isEnabled()) {
                if (StringUtils.isNotEmpty(customerUpdateProfile.getFirstName())) {
                    updateCustomer.setFirstName(customerUpdateProfile.getFirstName());
                }
                if (StringUtils.isNotEmpty(customerUpdateProfile.getLastName())) {
                    updateCustomer.setLastName(customerUpdateProfile.getLastName());
                }
                if (customerUpdateProfile.getPhoneNo() != null) {
                    updateCustomer.setPhoneNo(customerUpdateProfile.getPhoneNo());
                }
                if (StringUtils.isNotEmpty(customerUpdateProfile.getProfileURL())) {
                    updateCustomer.setProfileURL(customerUpdateProfile.getProfileURL());
                }
                if (StringUtils.isNotEmpty(customerUpdateProfile.getStreet())) {
                    updateCustomer.setStreet(customerUpdateProfile.getStreet());
                }
                if (StringUtils.isNotEmpty(customerUpdateProfile.getCity())) {
                    updateCustomer.setCity(customerUpdateProfile.getCity());
                }
                if (StringUtils.isNotEmpty(customerUpdateProfile.getProvince())) {
                    updateCustomer.setProvince(customerUpdateProfile.getProvince());
                }
                if (customerUpdateProfile.getZipCode() != null) {
                    updateCustomer.setZipCode(customerUpdateProfile.getZipCode());
                }
                customerRepository.save(updateCustomer);
                return "Profile updated successfully";
            }else{
                return "Please verify your email";
            }
        }
        return "Customer not found";
    }
    @Transactional
    public String deleteAcc(Integer id, String password) {
        try {
            Optional<Customer> customerOptional = customerRepository.findById(id);

            if (customerOptional.isEmpty()) {
                throw new IllegalStateException("User not found");
            }

            System.out.println("customer from deleting: "+customerOptional);
            Customer customer = customerOptional.get();

            if (passwordEncoder.matches(password, customer.getPassword())) {
                customerRepository.deleteById(id);
                return "Deleted successfully";
            } else {
                return "Password is incorrect";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}