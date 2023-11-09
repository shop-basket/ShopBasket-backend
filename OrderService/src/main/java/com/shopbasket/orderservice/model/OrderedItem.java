package com.shopbasket.orderservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_table")
public class OrderedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "pid")
    private Long pid;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    @JsonBackReference
    private Order order;

    public OrderedItem(Long pid, Integer quantity) {
        this.pid = pid;
        this.quantity = quantity;
    }
}
