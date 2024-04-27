package com.qikserve.inventoryControl.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private String id;

    @Column(name="totalPrice")
    private double totalPrice;

    @Column(name="quantity")
    private int quantiy;

    @Column(name="customer_id")
    private Customer customer;
}
