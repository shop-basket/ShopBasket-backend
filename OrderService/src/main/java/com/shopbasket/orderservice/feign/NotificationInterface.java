package com.shopbasket.orderservice.feign;

import com.shopbasket.orderservice.model.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationInterface {

    @PostMapping("/notifications/send")
    void sendNotification(@RequestBody NotificationRequest request);
}

