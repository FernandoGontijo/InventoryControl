package com.qikserve.inventoryControl.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCart {

    @Id
    private String id;

//
//    @ManyToOne
//    @JoinColumn(name = "cart_id")
//    private Cart cart;

//
//    @JoinColumn(name = "product_id")
//    private Product product;
}
