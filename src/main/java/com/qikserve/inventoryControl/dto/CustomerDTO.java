package com.qikserve.inventoryControl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class CustomerDTO implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;


    public CustomerDTO() {
    }

    public CustomerDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
