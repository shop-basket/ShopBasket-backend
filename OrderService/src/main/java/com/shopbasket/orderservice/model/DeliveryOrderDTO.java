package com.shopbasket.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderDTO {
    private Long oid;
    private Double totalAmount;
    private String deliveryAddress;
    private LocalDateTime deliveryDate;
    private Long cid;
    private Long omid;
    private Long wkid;
    private Long dpid;
    private Long cshrid;
}
