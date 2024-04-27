package com.qikserve.inventoryControl.repository;

import com.qikserve.inventoryControl.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {



    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(Product product);

}
