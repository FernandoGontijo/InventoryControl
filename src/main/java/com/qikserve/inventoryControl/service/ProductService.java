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
                    throw new RuntimeException(e);
                }

            });

        } catch (Exception e) {
            throw new Exception("Error fetching products.");
        }
    }

    private void getProductsInformations(String id) throws Exception {

        try {

            String productsInfoURL = WIREMOCK_URL + "/products/" + id;
            ResponseEntity<Product> responseInfo = restTemplate.getForEntity(productsInfoURL, Product.class);
            Product productInfo = responseInfo.getBody();

            Product product = new Product();
            product.setId(productInfo.getId());
            product.setName(productInfo.getName());
            product.setPrice(productInfo.getPrice());

            if (product.getPromotions() != null && !product.getPromotions().isEmpty()) {

                List<Promotion> promotions = new ArrayList<>();

                for (Promotion promotion : product.getPromotions()) {
                    Promotion promotionDTO = new Promotion();
                    promotion.setId(promotion.getId());
                    promotion.setType(promotionDTO.getType());
                    promotion.setRequired_qty(promotionDTO.getRequired_qty());
                    promotion.setPrice(promotionDTO.getPrice());

                    promotions.add(promotion);

                    promotionRepository.save(promotion);

                }
                product.setPromotions(promotions);

                productRepository.save(product);
            }
        } catch (Exception e) {
            throw new Exception("Error fetching products informations.");
        }
    }








}
