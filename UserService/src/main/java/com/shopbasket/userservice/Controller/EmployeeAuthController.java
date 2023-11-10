package com.shopbasket.userservice.Controller;

import com.shopbasket.userservice.DTO.*;
import com.shopbasket.userservice.Service.EmployeeAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(employeeAuthService.confirmToken(token));
    }
    @DeleteMapping(path = "/deleteAcc/{id}")
    public ResponseEntity<String> deleteAcc(@PathVariable("id") Integer id,
                            @RequestParam("password") String password){
        return ResponseEntity.ok(employeeAuthService.deleteAcc(id,password));
    }
    @PutMapping(path = "/updateProfile/{id}")
    public ResponseEntity<String> updateProfile(@PathVariable("id") Integer id,
                                                @RequestBody EmployeeUpdateProfile employeeUpdateProfile){
        return ResponseEntity.ok(employeeAuthService.updateProfile(id,employeeUpdateProfile));
    }
//    @PostMapping("/forgetPassword")
//    public ResponseEntity<String > forgetPassword(@RequestBody String email){
//        return ResponseEntity.ok(employeeAuthService.forgetPassword(email));
//    }
}
