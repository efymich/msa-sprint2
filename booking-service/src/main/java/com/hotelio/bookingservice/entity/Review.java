package com.hotelio.bookingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String hotelId;
    private String userId;

    @Column(length = 2000)
    private String text;

    private int rating; // от 1 до 5
    private LocalDate createdAt;
}
