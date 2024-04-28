package com.qikserve.inventoryControl.repository;

import com.qikserve.inventoryControl.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository  extends JpaRepository<Promotion, String> {



    List<Promotion> findAll();

    @Query(value = "SELECT * FROM Promotion WHERE product_id = :id", nativeQuery = true)
    List<Promotion> findAllByProduct(String id);

    Optional<Promotion> findById(String id);

    Promotion save(Promotion promotion);
}
