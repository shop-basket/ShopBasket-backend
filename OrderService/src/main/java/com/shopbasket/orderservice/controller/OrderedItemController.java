package com.shopbasket.orderservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ShopBasket/api/order-service")
@CrossOrigin(allowedHeaders = "*" ,origins = "*")
public class OrderedItemController {
}
