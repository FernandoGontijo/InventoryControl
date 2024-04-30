package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qikserve.inventoryControl.model.Product;

import java.io.Serializable;

public class PromotionDTO implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("required_qty")
    private int requiredQty;

    @JsonProperty("price")
    private int price;

    @JsonProperty("free_qty")
    private int freeQty;

    @JsonProperty("product")
    private Product product;

    public PromotionDTO() {
    }


    public PromotionDTO(String id, String type, int amount, int requiredQty, int price, int freeQty, Product product) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.requiredQty = requiredQty;
        this.price = price;
        this.freeQty = freeQty;
        this.product = product;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getRequiredQty() {
        return requiredQty;
    }

    public void setRequiredQty(int requiredQty) {
        this.requiredQty = requiredQty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
