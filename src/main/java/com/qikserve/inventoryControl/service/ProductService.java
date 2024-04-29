package com.qikserve.inventoryControl.service;


import com.qikserve.inventoryControl.dto.ProductDTO;
import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.repository.ProductRepository;
import com.qikserve.inventoryControl.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;




    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTO = new ArrayList<>();
        products.forEach(product -> productDTO.add(Util.modelMapper.map(product, ProductDTO.class)));
        return productDTO;
    }

    public ProductDTO findBy(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return Util.modelMapper.map(product.get(), ProductDTO.class);
        } else {
            throw new EntityNotFoundException("Product not found!");
        }
    }

    public ProductDTO insert(ProductDTO productDTO) {
        checkProduct(productDTO);
        Product product = Util.modelMapper.map(productDTO, Product.class);
        product.setId(Util.createID());
        Product productCreated = productRepository.save(product);
        return Util.modelMapper.map(productCreated, ProductDTO.class);
    }

    public ProductDTO update(ProductDTO productDTO) {
        ProductDTO productToUpdate = findBy(productDTO.id());
        Product product = new Product();
        product.setId(productToUpdate.id());
        product.setPrice(productDTO.price());
        product.setName(productDTO.name());
        productRepository.save(product);
        return Util.modelMapper.map(product, ProductDTO.class);
    }

    public void remove(String id) {
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
