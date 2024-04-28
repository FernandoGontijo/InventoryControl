package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record CustomerDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name
) implements Serializable {}