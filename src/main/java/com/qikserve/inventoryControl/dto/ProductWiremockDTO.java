package com.qikserve.inventoryControl.dto;

import com.qikserve.inventoryControl.model.Promotion;


import java.util.List;

public record ProductWiremockDTO (String id, String name, int price, List<Promotion> promotions) {}

