package com.qikserve.inventoryControl;

import com.qikserve.inventoryControl.service.WiremockService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryControlApplication {

	@Autowired
	private WiremockService wiremockService;

	public static void main(String[] args) {
		SpringApplication.run(InventoryControlApplication.class, args);
	}

	@PostConstruct
	public void init() throws Exception {
		wiremockService.getProductsFromWiremock();
	}

}
