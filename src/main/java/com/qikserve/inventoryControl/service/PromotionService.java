package com.qikserve.inventoryControl.service;


import com.qikserve.inventoryControl.dto.PromotionDTO;
import com.qikserve.inventoryControl.model.Promotion;
import com.qikserve.inventoryControl.repository.PromotionRepository;
import com.qikserve.inventoryControl.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductService productService;




    public List<PromotionDTO> findAll() {
        List<Promotion> promotions = promotionRepository.findAll();
        List<PromotionDTO> promotionsDTO = new ArrayList<>();
        promotions.forEach(promotion -> promotionsDTO.add(Util.modelMapper.map(promotion, PromotionDTO.class)));
        return promotionsDTO;
    }

    public List<PromotionDTO> findAllByProduct(String id) {
        List<Promotion> promotions = promotionRepository.findAllByProduct(id);
        List<PromotionDTO> promotionsDTO = new ArrayList<>();
        promotions.forEach(promotion -> promotionsDTO.add(Util.modelMapper.map(promotion, PromotionDTO.class)));
        return promotionsDTO;
    }

    public PromotionDTO findBy(String id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isPresent()) {
            return Util.modelMapper.map(promotion.get(), PromotionDTO.class);
        } else {
            throw new EntityNotFoundException("Promotion not found!");
        }
    }

    public PromotionDTO insert(PromotionDTO promotionDTO) {
        checkProduct(promotionDTO);
        Promotion promotion = Util.modelMapper.map(promotionDTO, Promotion.class);
        promotion.setId(Util.createID());
        Promotion promotionCreated = promotionRepository.save(promotion);
        return Util.modelMapper.map(promotionCreated, PromotionDTO.class);
    }

    public PromotionDTO update(PromotionDTO promotionDTO) {
        PromotionDTO promotionToUpdate = findBy(promotionDTO.id());
        Promotion promotion = new Promotion();
        promotion.setId(promotionToUpdate.id());
        promotion.setPrice(promotionDTO.price());
        promotion.setAmount(promotionDTO.amount());
        promotion.setRequired_qty(promotionToUpdate.required_qty());
        promotion.setType(promotionDTO.type());
        promotion.setProduct(promotionDTO.product());
        promotionRepository.save(promotion);
        return Util.modelMapper.map(promotion, PromotionDTO.class);
    }

    public void remove(String id) {
        PromotionDTO promotionToRemove = findBy(id);
        promotionRepository.delete(Util.modelMapper.map(promotionToRemove, Promotion.class));
    }

    private void checkProduct(PromotionDTO promotionDTO) throws IllegalArgumentException {

        if (promotionDTO == null) {
            throw new IllegalArgumentException("Invalid promotion!");
        }
        if (promotionDTO.type() == null || promotionDTO.type().isEmpty()) {
            throw new IllegalArgumentException("Invalid promotion type!");
        }
        if (promotionDTO.price() <= 0) {
            throw new IllegalArgumentException("Invalid promotion price!");
        }
        if (promotionDTO.product() == null) {
            throw new IllegalArgumentException("Product not found!");
        }
    }



}
