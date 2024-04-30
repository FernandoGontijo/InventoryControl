package com.qikserve.inventoryControl.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Promotion {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="type")
    private String type;

    @Column(name="amount")
    private int amount;

    @Column(name="required_qty")
    private int required_qty;

    @Column(name="price")
    private int price;

    @Column(name="free_qty")
    private int free_qty;

}
