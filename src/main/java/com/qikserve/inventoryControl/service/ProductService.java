package com.qikserve.inventoryControl.service;


import com.qikserve.inventoryControl.dto.ProductDTO;
import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.repository.ProductRepository;
import com.qikserve.inventoryControl.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    private static final Logger logger = LogManager.getLogger(ProductService.class);


    public List<ProductDTO> findAll() {
        logger.debug("Finding all products");
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTO = new ArrayList<>();
        products.forEach(product -> productDTO.add(Util.modelMapper.map(product, ProductDTO.class)));
        return productDTO;
    }

    public ProductDTO findBy(String id) {
        logger.debug("Finding product by ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return Util.modelMapper.map(product.get(), ProductDTO.class);
        } else {
            logger.error("Product not found with ID: {}", id);
            throw new EntityNotFoundException("Product not found!");
        }
    }

    public ProductDTO insert(ProductDTO productDTO) {
        logger.debug("Inserting product: {}", productDTO);
        checkProduct(productDTO);
        Product product = Util.modelMapper.map(productDTO, Product.class);
        product.setId(Util.createID());
        Product productCreated = productRepository.save(product);
        return Util.modelMapper.map(productCreated, ProductDTO.class);
    }

    public ProductDTO update(ProductDTO productDTO, String id) {
        logger.debug("Updating product with ID: {}", id);
        ProductDTO productToUpdate = findBy(id);
        Product product = new Product();
        product.setId(productToUpdate.id());
        product.setPrice(productDTO.price());
        product.setName(productDTO.name());
        productRepository.save(product);
        return Util.modelMapper.map(product, ProductDTO.class);
    }

    public void remove(String id) {
        logger.debug("Removing product with ID: {}", id);
        ProductDTO productToRemove = findBy(id);
        productRepository.delete(Util.modelMapper.map(productToRemove, Product.class));
    }

    public void checkProduct(ProductDTO productDTO) throws IllegalArgumentException {

        if (productDTO == null) {
            throw new IllegalArgumentException("Invalid product!");
        }
        if (productDTO.name() == null || productDTO.name().isEmpty()) {
            throw new IllegalArgumentException("Invalid product name!");
        }
        if (productDTO.price() <= 0) {
            throw new IllegalArgumentException("Invalid product price!");
        }
    }


}
