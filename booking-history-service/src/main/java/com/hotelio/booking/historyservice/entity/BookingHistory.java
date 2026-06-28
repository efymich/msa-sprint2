package com.hotelio.booking.historyservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
public class BookingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String userId;
    private String hotelId;

    private String promoCode;
    private Double discountPercent;

    @Column(nullable = false)
    private Double price;

    private Instant createdAt;

}

