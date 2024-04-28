package com.qikserve.inventoryControl.repository;

import com.qikserve.inventoryControl.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {



    List<Product> findAll();

    Optional<Product> findById(String id);

    Product save(Product product);

}
