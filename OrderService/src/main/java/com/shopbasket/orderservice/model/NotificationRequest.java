package com.shopbasket.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationRequest {

    private Long orderId;
    private Long warehouseKeeperId;
    private String message;

}
