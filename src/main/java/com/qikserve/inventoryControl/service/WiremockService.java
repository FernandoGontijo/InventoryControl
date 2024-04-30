package com.qikserve.inventoryControl.service;

import com.qikserve.inventoryControl.dto.ProductWiremockDTO;
import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.model.Promotion;
import com.qikserve.inventoryControl.repository.ProductRepository;
import com.qikserve.inventoryControl.repository.PromotionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WiremockService {

    private static final Logger logger = LogManager.getLogger(WiremockService.class);

    private static final String WIREMOCK_URL = "http://localhost:8081";

    private final RestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;


    @Autowired
    public WiremockService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void getProductsFromWiremock() throws Exception {
        logger.debug("Getting products from wiremock");
        try {
            String productsURL = WIREMOCK_URL + "/products";

            ResponseEntity<ProductWiremockDTO[]> responseProductEntity =
                    restTemplate.getForEntity(productsURL, ProductWiremockDTO[].class);

            ProductWiremockDTO[] productsArray = responseProductEntity.getBody();

            List<ProductWiremockDTO> products = Arrays.asList(productsArray);

            products.forEach(product -> {

                try {
                    logger.debug("Getting more details for product {}", product.name());
                    fetchProductsInformations(product.id());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void fetchProductsInformations(String id) throws Exception {

        try {
            String productsInfoURL = WIREMOCK_URL + "/products/" + id;
            ResponseEntity<ProductWiremockDTO> responseInfo = restTemplate.getForEntity(productsInfoURL, ProductWiremockDTO.class);
            ProductWiremockDTO productInfo = responseInfo.getBody();

            Product product = saveProduct(productInfo);
            savePromotions(productInfo, product);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    private Product saveProduct(ProductWiremockDTO productInfo) {
        logger.debug("Saving product {}", productInfo.name());
        Product product = new Product();
        product.setId(productInfo.id());
        product.setName(productInfo.name());
        product.setPrice(productInfo.price());
        productRepository.save(product);
        return product;
    }

    private List<Promotion> savePromotions(ProductWiremockDTO productInfo, Product product) {
        logger.debug("Saving promotions for product {}", productInfo.name());
        List<Promotion> promotions = new ArrayList<>();
        Promotion promotionToSave = new Promotion();
        if (productInfo.promotions() != null && !productInfo.promotions().isEmpty()) {
            for (Promotion promotion : productInfo.promotions()) {
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
        return promotions;
    }
}
