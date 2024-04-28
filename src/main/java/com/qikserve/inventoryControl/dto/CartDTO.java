package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qikserve.inventoryControl.model.Customer;
import com.qikserve.inventoryControl.model.Product;

import java.io.Serializable;
import java.util.List;

public record CartDTO(
        @JsonProperty("id") String id,
        @JsonProperty("totalPrice") double totalPrice,
        @JsonProperty("quantity") int quantity,
        @JsonProperty("customer_id") Customer customer,
        @JsonProperty("products") List<Product> products
) implements Serializable {}
