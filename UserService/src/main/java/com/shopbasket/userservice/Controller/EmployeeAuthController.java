package com.shopbasket.userservice.Controller;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Service.EmployeeAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ShopBasket/api/auth")
@RequiredArgsConstructor
public class EmployeeAuthController {
    private final EmployeeAuthService employeeAuthService;
    @PostMapping("/authenticate")
    public ResponseEntity <AuthenticationResponse>authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(employeeAuthService.empAuthenticate(request));
    }
    @PostMapping("/changePassword")
    public ResponseEntity<MessageResponse> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ){
        System.out.println("changePassword:"+changePasswordRequest);
        return ResponseEntity.ok(employeeAuthService.changePassword(changePasswordRequest));
    }
}
