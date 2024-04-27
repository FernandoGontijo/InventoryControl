package com.qikserve.inventoryControl.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private String id;

    @Column(name="type")
    private String type;

    @Column(name="amount")
    private int amount;

    @Column(name="required_qty")
    private int required_qty;

    @Column(name="price")
    private double price;

}
