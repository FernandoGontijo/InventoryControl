package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.qikserve.inventoryControl.model.Promotion;
import lombok.*;

import java.io.Serializable;
import java.util.List;

public record ProductWiremockDTO (
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("price") double price,
        @JsonProperty("promotions") List<Promotion> promotions
) implements Serializable {}

