package com.example.demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GuestCountEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private int guestCount =0;

    public void addCount(){
        this.guestCount += 1;
    }
}
