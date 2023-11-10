package com.shopbasket.userservice.Controller;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ShopBasket/api/customerAuth")
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping(path = "/register")
    public ResponseEntity <AuthenticationResponse>register(
            @RequestBody CustomerRegisterRequest request
    ){
        return ResponseEntity.ok(customerService.customerRegister(request));
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> customerAuthenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(customerService.customerAuthenticate(request));
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<MessageResponse> customerChangePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ){
        return ResponseEntity.ok(customerService.changePassword(changePasswordRequest));
    }
    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(customerService.confirmToken(token));
    }
    @DeleteMapping(path = "/deleteAcc/{id}")
    public ResponseEntity<String> deleteAcc(@PathVariable("id") Integer id,
                            @RequestParam("password") String password){
        return ResponseEntity.ok(customerService.deleteAcc(id,password));
    }
    @PutMapping(path = "/updateProfile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable("id") Integer id,
                                                @RequestBody CustomerUpdateProfile customerUpdateProfile){
        return ResponseEntity.ok(customerService.updateProfile(id,customerUpdateProfile));
    }
}
