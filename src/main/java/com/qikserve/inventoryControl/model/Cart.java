package com.qikserve.inventoryControl.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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
    private double totalPrice;

    @Column(name="totalSavings")
    private double totalSavings;

    @Column(name="quantity")
    private int quantiy;

    @OneToOne
    @JoinColumn(name="customer_id")
    private Customer customer;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ProductCart", joinColumns =
    @JoinColumn(name = "cart_id"), inverseJoinColumns =
    @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

}
