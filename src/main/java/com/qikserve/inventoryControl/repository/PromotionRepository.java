package com.qikserve.inventoryControl.repository;

import com.qikserve.inventoryControl.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository  extends JpaRepository<Promotion, String> {



    List<Promotion> findAll();

    Optional<Promotion> findById(String id);

    Promotion save(Promotion promotion);
}
