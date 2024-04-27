package com.qikserve.inventoryControl.service;

import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductService {


    private static final String WIREMOCK_URL = "http://localhost:8081";

    @Autowired
    private ProductRepository repo;

    private final RestTemplate restTemplate;


    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getProductsFromWiremock() {

        String productsURL = WIREMOCK_URL + "/products";
        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(productsURL, Product[].class);
        Product[] productsArray = responseEntity.getBody();

        // Convertendo o array de produtos para uma lista
        List<Product> products = Arrays.asList(productsArray);

        return products;
    }








}
