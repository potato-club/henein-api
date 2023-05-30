package com.example.demo.entity;

import com.example.demo.enumCustom.AchieveType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class AchieveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private AchieveType achieveType;
    @Column
    private String colorCode;
    @Column
    private String achieveName;

}
