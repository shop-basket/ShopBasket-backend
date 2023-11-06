package com.shopbasket.userservice.Controller;

import com.shopbasket.userservice.DTO.EmployeeCreationResponse;
import com.shopbasket.userservice.Service.SystemAdminService;
import com.shopbasket.userservice.Entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ShopBasket/api/systemAdmin")
public class SystemAdminController {
    @Autowired
    private final SystemAdminService systemAdminService;
    public SystemAdminController(SystemAdminService systemAdminService) {
        this.systemAdminService = systemAdminService;
    }
    @PostMapping("/createEmployee")
    public Employee createEmployee(@RequestBody EmployeeCreationResponse employeeCreationResponse ) {
        return systemAdminService.saveEmployee(employeeCreationResponse);
    }
}
