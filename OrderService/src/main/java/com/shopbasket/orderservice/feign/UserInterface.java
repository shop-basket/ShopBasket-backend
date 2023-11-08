package com.shopbasket.orderservice.feign;

import com.shopbasket.orderservice.model.CustomerWrapper;
import com.shopbasket.orderservice.model.EmployeeWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("User-Service")
public interface UserInterface {
    @GetMapping("customer/{cid}")
    ResponseEntity<CustomerWrapper> getCustomerById (@PathVariable Long cid);

    @GetMapping("om/{cid}")
    ResponseEntity<EmployeeWrapper> getOmById (@PathVariable Long cid);

    @GetMapping("wk/{cid}")
    ResponseEntity<EmployeeWrapper> getWkById (@PathVariable Long cid);

    @GetMapping("dp/{cid}")
    ResponseEntity<EmployeeWrapper> getDpById (@PathVariable Long cid);

    @GetMapping("cashier/{cid}")
    ResponseEntity<EmployeeWrapper> getCshrById (@PathVariable Long cid);
}
