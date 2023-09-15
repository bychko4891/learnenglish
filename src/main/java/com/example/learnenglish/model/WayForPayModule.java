package com.example.learnenglish.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "way_for_pay_module")
public class WayForPayModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private String merchantAccount;

    private String merchantSecretKEY;

    private boolean isActive = false;



}
