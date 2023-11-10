package com.shopbasket.userservice.Controller;

import com.shopbasket.userservice.DTO.AuthenticationResponse;
import com.shopbasket.userservice.DTO.CustomerRegisterRequest;
import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import com.shopbasket.userservice.DTO.RegisterRequest;
import com.shopbasket.userservice.Repository.Role;
import com.shopbasket.userservice.Service.CustomerService;
import com.shopbasket.userservice.Service.EmployeeAuthService;
import com.shopbasket.userservice.Service.SystemAdminService;
import com.shopbasket.userservice.Entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ShopBasket/api/systemAdmin")
public class SystemAdminController {
    @Autowired
    private final SystemAdminService systemAdminService;
//    TEMPORARY
    @Autowired
    private EmployeeAuthService employeeAuthService;

    public SystemAdminController(SystemAdminService systemAdminService) {
        this.systemAdminService = systemAdminService;
    }
    @PostMapping("/createEmployee")
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeCreationResponse employeeCreationResponse ) {
        return ResponseEntity.ok(systemAdminService.saveEmployee(employeeCreationResponse));
    }

    @PutMapping("/modifyUsers/{id}")
    public ResponseEntity<String> modifyUsers(@PathVariable("id") Integer id,
                                              @RequestParam("token") String token,
                                              @RequestParam("newRole") Role newRole){
        return ResponseEntity.ok(systemAdminService.modifyUsers(id,token, newRole));
    }

//    TEMPORARY
    @PostMapping("/addAdmin")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(employeeAuthService.register(request));
    }

}
