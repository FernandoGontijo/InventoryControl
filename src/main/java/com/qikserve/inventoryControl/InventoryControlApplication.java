package com.qikserve.inventoryControl;

import com.qikserve.inventoryControl.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.util.List;

@SpringBootApplication
public class InventoryControlApplication {

	@Autowired
	private ProductService productService;

	public static void main(String[] args) {
		SpringApplication.run(InventoryControlApplication.class, args);
	}

	@PostConstruct
	public void init() {
		productService.getProductsFromWiremock();
	}

}
