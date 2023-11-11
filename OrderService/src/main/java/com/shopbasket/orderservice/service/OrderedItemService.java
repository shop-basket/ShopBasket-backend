package com.shopbasket.orderservice.service;


import com.shopbasket.orderservice.feign.ProductInterface;
import com.shopbasket.orderservice.model.Order;
import com.shopbasket.orderservice.model.OrderedItem;
import com.shopbasket.orderservice.model.ProductWrapper;
import com.shopbasket.orderservice.repository.OrderedItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderedItemService {

    ProductInterface productInterface;
    private final OrderedItemRepository orderedItemRepository;
    private final OrderService orderService;

    @Autowired
    public OrderedItemService(OrderedItemRepository orderedItemRepository, OrderService orderService) {
        this.orderedItemRepository = orderedItemRepository;
        this.orderService = orderService;
    }

    public ResponseEntity<ProductWrapper> getProduct(String skuCode){
        ResponseEntity<ProductWrapper> product = productInterface.getProductBySkuCode(skuCode);
        return product;
    }

    public OrderedItem addItem(Long oid,OrderedItem item) {
        item.setOrder(orderService.getOrderByOid(oid));
        return orderedItemRepository.save(item);
    }

}
