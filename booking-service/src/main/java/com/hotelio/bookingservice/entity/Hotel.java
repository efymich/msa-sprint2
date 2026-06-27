package com.hotelio.bookingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hotel {

    @Id
    private String id;

    private boolean operational;
    private boolean fullyBooked;

    private String city;
    private double rating;

    @Column(length = 1000)
    private String description;
}
