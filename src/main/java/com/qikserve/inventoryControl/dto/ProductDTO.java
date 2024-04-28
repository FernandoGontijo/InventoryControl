package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public record ProductDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("price") double price
) implements Serializable {}
