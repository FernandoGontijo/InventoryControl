package com.qikserve.inventoryControl.controller;

import com.qikserve.inventoryControl.dto.PromotionDTO;
import com.qikserve.inventoryControl.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.findAll();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable String id) {
        PromotionDTO promotion = promotionService.findBy(id);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<PromotionDTO>> getPromotionsByProduct(@PathVariable String productId) {
        List<PromotionDTO> promotions = promotionService.findAllByProduct(productId);
        return ResponseEntity.ok(promotions);
    }

    @PostMapping
    public ResponseEntity<PromotionDTO> createPromotion(@RequestBody PromotionDTO promotionDTO) {
        PromotionDTO createdPromotion = promotionService.insert(promotionDTO);
        return ResponseEntity.ok(createdPromotion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionDTO> updatePromotion(@PathVariable String id,
                                                        @RequestBody PromotionDTO promotionDTO) {
        PromotionDTO updatedPromotion = promotionService.update(promotionDTO, id);
        return ResponseEntity.ok(updatedPromotion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable String id) {
        promotionService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
