package com.shopbasket.orderservice.service;

import com.shopbasket.orderservice.exception.CustomNotFoundException;
import com.shopbasket.orderservice.exception.TotalAmountException;
import com.shopbasket.orderservice.feign.ProductInterface;
import com.shopbasket.orderservice.model.Order;
import com.shopbasket.orderservice.model.OrderStatus;
import com.shopbasket.orderservice.model.OrderedItem;
import com.shopbasket.orderservice.repository.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private ProductInterface productInterface;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Double calculateTotalAmount(List<OrderedItem> items){
        Double totalAmount = 0.0;
//        for (OrderedItem item : items) {
//            totalAmount += productInterface.getProductByPid(item.getPid()).getBody().getUnitPrice();
//        }
        return totalAmount;
    }

    @Transactional
    public Order createOrder(Order order) {
        Double totalAmount = calculateTotalAmount(order.getItems());
        if (totalAmount < 0) {
            throw new TotalAmountException("Total amount cannot be negative.");
        }
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    public Order setOM(Long oid, Long eid) {
        Optional<Order> optionalOrder = orderRepository.findById(oid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOmid(eid);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order setWK(Long oid, Long eid) {
        Optional<Order> optionalOrder = orderRepository.findById(oid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setWkid(eid);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order setDP(Long oid, Long eid) {
        Optional<Order> optionalOrder = orderRepository.findById(oid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setDpid(eid);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order setCshr(Long oid, Long eid) {
        Optional<Order> optionalOrder = orderRepository.findById(oid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setCshrid(eid);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order setStatus(Long oid, OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findById(oid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order addItem(Long oid, OrderedItem item) {
        Optional<Order> optionalOrder = orderRepository.findById(oid);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            List<OrderedItem> currentItems = order.getItems();
            currentItems.add(item);
            order.setTotalAmount(calculateTotalAmount(currentItems));
            order.setItems(currentItems);
            return orderRepository.save(order);
        }
        return null;
    }

    public List<Order> getOrderByCid(Long cid) {
        return orderRepository.findOrdersByCustomerId(cid);
    }

    public Order getOrderByOid(Long oid) {
        return orderRepository.findById(oid)
                .orElseThrow(() -> new CustomNotFoundException("Order with ID " + oid + " not found"));
    }
}
