package com.hotelio.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class PromoCode {

    @Id
    private String code;

    private double discount;
    private boolean vipOnly;
    private boolean expired;

    private LocalDate validUntil;
    private String description;
}
