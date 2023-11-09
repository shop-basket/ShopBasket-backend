package com.shopbasket.userservice.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationEmailToken {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
//    @ManyToOne
//    @JoinColumn(nullable = false, name = "user_id")
    private Integer userId;

    public ConfirmationEmailToken(String token, LocalDateTime createdAt,
                                  LocalDateTime expiresAt,Integer userId) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }
}

