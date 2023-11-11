package com.shopbasket.userservice.Controller;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ShopBasket/api/customerAuth")
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity <AuthenticationResponse>register(
            @RequestBody CustomerRegisterRequest request
    ){
        return ResponseEntity.ok(customerService.customerRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> customerAuthenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(customerService.customerAuthenticate(request));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<MessageResponse> customerChangePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ){
        return ResponseEntity.ok(customerService.changePassword(changePasswordRequest));
    }
}
