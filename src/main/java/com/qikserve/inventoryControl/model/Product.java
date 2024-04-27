package com.qikserve.inventoryControl.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @Column(name="promotion")
    private Promotion promotion;

}
