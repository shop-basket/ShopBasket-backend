package com.shopbasket.userservice.Demo;

import com.shopbasket.userservice.DTO.AuthenticationResponse;
import com.shopbasket.userservice.DTO.RegisterRequest;
import com.shopbasket.userservice.Service.EmployeeAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ShopBasket/demo/demo-controller")
public class DemoController {
    private EmployeeAuthService employeeAuthService;
    @GetMapping
    public ResponseEntity<String> sayHello(){

        return ResponseEntity.ok("Hello I am from secured endpoint");
    }
}
