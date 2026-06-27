package com.hotelio.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class AppUser {

    @Id
    private String id;

    private String status;
    private boolean blacklisted;
    private boolean active;

    private String name;
    private String email;
    private String city;

    public AppUser() {}

    public AppUser(String id, String status, boolean blacklisted, boolean active) {
        this.id = id;
        this.status = status;
        this.blacklisted = blacklisted;
        this.active = active;
    }
}
