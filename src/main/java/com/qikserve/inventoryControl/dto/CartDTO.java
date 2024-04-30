package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qikserve.inventoryControl.model.Customer;
import com.qikserve.inventoryControl.model.Product;

import java.io.Serializable;
import java.util.List;

public class CartDTO implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("totalPrice")
    private int totalPrice;

    @JsonProperty("totalSavings")
    private int totalSavings;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("customer_id")
    private Customer customer;

    @JsonProperty("products")
    private List<Product> products;


    public CartDTO() {
    }


    public CartDTO(String id, int totalPrice, int totalSavings, int quantity, Customer customer, List<Product> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.totalSavings = totalSavings;
        this.quantity = quantity;
        this.customer = customer;
        this.products = products;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(int totalSavings) {
        this.totalSavings = totalSavings;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
