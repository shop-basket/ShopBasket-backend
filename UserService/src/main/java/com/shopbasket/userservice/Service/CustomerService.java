package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.DTO.AuthenticationRequest;
import com.shopbasket.userservice.DTO.AuthenticationResponse;
import com.shopbasket.userservice.DTO.CustomerRegisterRequest;
import com.shopbasket.userservice.Entities.Customer;
import com.shopbasket.userservice.Entities.Employee;
import com.shopbasket.userservice.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
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

//    public AuthenticationResponse customerAuthenticate(AuthenticationRequest request) {
//        System.out.println("Customer Login from Service : ");
//        System.out.println(request.getEmail()+request.getPassword());
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//        System.out.println("_______________");
//        System.out.println("before findByEmail"+request.getEmail());
//        System.out.println(customerRepository.findByEmail(request.getEmail()));
//        var customer = customerRepository.findByEmail(request.getEmail())
//                .orElseThrow();
//        System.out.println(customer);
//        var jwtToken =  jwtService.generateTokenForCustomer(customer);
//        System.out.println("Token: "+jwtToken);
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }

    public AuthenticationResponse customerRegister(CustomerRegisterRequest registerRequest) {
        var fetchCustomer = customerRepository.findByEmail(registerRequest.getEmail());
        boolean notExistsCustomer = fetchCustomer.isEmpty();
        boolean validCredentials = userValidationService.customerCredentialValidation(registerRequest);
        System.out.println("zip code:"+registerRequest.getZIPCode());

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
                    .ZIPCode(registerRequest.getZIPCode())
                    .build();
            System.out.println("Customer Register:"+customer);
            customerRepository.save(customer);
            var jwtToken = jwtService.generateTokenForCustomer(customer);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }else{
            return AuthenticationResponse.builder()
                    .message("User with this email is already registered")
                    .build();
        }
    }
}
