package com.qikserve.inventoryControl.dto;

import com.qikserve.inventoryControl.model.Promotion;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Component
public class ProductDTO implements Serializable {

    private static final long serialVersionUUID = 1L;

    private String id;

    private String name;

    private double price;

    private List<Promotion> promotions;
}
