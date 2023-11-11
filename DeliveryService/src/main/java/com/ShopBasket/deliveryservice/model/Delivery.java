package com.ShopBasket.deliveryservice.model;


import com.ShopBasket.deliveryservice.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courierId;
    private Long orderManagerId;
    private Long orderId;
    private Date date;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.ACCEPTED;
}
