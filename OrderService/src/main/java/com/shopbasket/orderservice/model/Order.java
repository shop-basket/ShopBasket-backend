package com.shopbasket.orderservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Long oid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderedItem> items;

    @Column(name = "total_amount")
    private Double totalAmount;

    @CreationTimestamp
    @Column(name = "date&time")
    private LocalDateTime createdDateTime;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "cid")
    private Long cid;

    @Column(name = "om_id")
    private Long omid;

    @Column(name = "wk_id")
    private Long wkid;

    @Column(name = "dp_id")
    private Long dpid;

    @Column(name = "cshr_id")
    private Long cshrid;


    public Order(OrderStatus status, List<OrderedItem> items, String deliveryAddress, LocalDateTime deliveryDate, Long cid) {
        this.status = status;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDate = deliveryDate;
        this.cid = cid;
    }
}
