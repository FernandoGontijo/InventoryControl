package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qikserve.inventoryControl.model.Product;


import java.io.Serializable;




public record PromotionDTO(
        @JsonProperty("id") String id,
        @JsonProperty("type") String type,
        @JsonProperty("amount") int amount,
        @JsonProperty("required_qty") int required_qty,
        @JsonProperty("price") double price,
        @JsonProperty("product") Product product

) implements Serializable {}


