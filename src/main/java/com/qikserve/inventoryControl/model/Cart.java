package com.qikserve.inventoryControl.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    private String id;

    @Column(name="totalPrice")
    private int totalPrice;

    @Column(name="totalSavings")
    private int totalSavings;

    @Column(name="quantity")
    private int quantity;

    @OneToOne
    @JoinColumn(name="customer_id")
    private Customer customer;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ProductCart", joinColumns =
    @JoinColumn(name = "cart_id"), inverseJoinColumns =
    @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

}
