package com.qikserve.inventoryControl.service;

import com.qikserve.inventoryControl.dto.ProductDTO;
import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.model.Promotion;
import com.qikserve.inventoryControl.repository.ProductRepository;
import com.qikserve.inventoryControl.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {


    private static final String WIREMOCK_URL = "http://localhost:8081";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    private final RestTemplate restTemplate;


    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getProductsFromWiremock() throws Exception {

        try {
            String productsURL = WIREMOCK_URL + "/products";

            ResponseEntity<ProductDTO[]> responseProductEntity =
                    restTemplate.getForEntity(productsURL, ProductDTO[].class);

            ProductDTO[] productsArray = responseProductEntity.getBody();

            List<ProductDTO> products = Arrays.asList(productsArray);

            products.forEach(product -> {

                try {
                    getProductsInformations(product.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }

            });

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void getProductsInformations(String id) throws Exception {

        try {

            List<Promotion> promotions = new ArrayList<>();
            Promotion promotionToSave = new Promotion();

            String productsInfoURL = WIREMOCK_URL + "/products/" + id;
            ResponseEntity<ProductDTO> responseInfo = restTemplate.getForEntity(productsInfoURL, ProductDTO.class);
            ProductDTO productInfo = responseInfo.getBody();

            Product product = new Product();
            product.setId(productInfo.getId());
            product.setName(productInfo.getName());
            product.setPrice(productInfo.getPrice());

            productRepository.save(product);

            if (productInfo.getPromotions() != null && !productInfo.getPromotions().isEmpty()) {

                for (Promotion promotion : productInfo.getPromotions()) {

                    promotionToSave.setId(promotion.getId());
                    promotionToSave.setType(promotion.getType());
                    promotionToSave.setRequired_qty(promotion.getRequired_qty());
                    promotionToSave.setPrice(promotion.getPrice());
                    promotionToSave.setAmount(promotion.getAmount());
                    promotionToSave.setProduct(product);

                    promotions.add(promotionToSave);
                }
                promotionRepository.save(promotionToSave);
            }

            System.out.println("Product save!");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }








}
