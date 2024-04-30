package com.qikserve.inventoryControl.service;


import com.qikserve.inventoryControl.dto.PromotionDTO;
import com.qikserve.inventoryControl.model.Promotion;
import com.qikserve.inventoryControl.repository.PromotionRepository;
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
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductService productService;


    private static final Logger logger = LogManager.getLogger(PromotionService.class);

    public List<PromotionDTO> findAll() {
        logger.debug("Finding all promotions");
        List<Promotion> promotions = promotionRepository.findAll();
        List<PromotionDTO> promotionsDTO = new ArrayList<>();
        promotions.forEach(promotion -> promotionsDTO.add(Util.modelMapper.map(promotion, PromotionDTO.class)));
        return promotionsDTO;
    }

    public List<PromotionDTO> findAllByProduct(String id) {
        logger.debug("Finding promotions by product ID: {}", id);
        List<Promotion> promotions = promotionRepository.findAllByProduct(id);
        List<PromotionDTO> promotionsDTO = new ArrayList<>();
        promotions.forEach(promotion -> promotionsDTO.add(Util.modelMapper.map(promotion, PromotionDTO.class)));
        return promotionsDTO;
    }

    public PromotionDTO findBy(String id) {
        logger.debug("Finding promotion by ID: {}", id);
        Optional<Promotion> promotion = promotionRepository.findById(id);
        if (promotion.isPresent()) {
            return Util.modelMapper.map(promotion.get(), PromotionDTO.class);
        } else {
            logger.error("Promotion not found with ID: {}", id);
            throw new EntityNotFoundException("Promotion not found!");
        }
    }

    public PromotionDTO insert(PromotionDTO promotionDTO) {
        logger.debug("Inserting promotion: {}", promotionDTO);
        checkPromotion(promotionDTO);
        Promotion promotion = Util.modelMapper.map(promotionDTO, Promotion.class);
        promotion.setId(Util.createID());
        Promotion promotionCreated = promotionRepository.save(promotion);
        return Util.modelMapper.map(promotionCreated, PromotionDTO.class);
    }

    public PromotionDTO update(PromotionDTO promotionDTO, String id) {
        logger.debug("Updating promotion with ID: {}", id);
        PromotionDTO promotionToUpdate = findBy(id);
        Promotion promotion = new Promotion();
        promotion.setId(promotionToUpdate.getId());
        promotion.setPrice(promotionDTO.getPrice());
        promotion.setAmount(promotionDTO.getAmount());
        promotion.setRequired_qty(promotionToUpdate.getRequiredQty());
        promotion.setType(promotionDTO.getType());
        promotion.setProduct(promotionDTO.getProduct());
        promotionRepository.save(promotion);
        return Util.modelMapper.map(promotion, PromotionDTO.class);
    }

    public void remove(String id) {
        logger.debug("Removing promotion with ID: {}", id);
        PromotionDTO promotionToRemove = findBy(id);
        promotionRepository.delete(Util.modelMapper.map(promotionToRemove, Promotion.class));
    }

    private void checkPromotion(PromotionDTO promotionDTO) throws IllegalArgumentException {

        if (promotionDTO == null) {
            throw new IllegalArgumentException("Invalid promotion!");
        }
        if (promotionDTO.getType() == null || promotionDTO.getType().isEmpty()) {
            throw new IllegalArgumentException("Invalid promotion type!");
        }
        if (promotionDTO.getPrice() <= 0) {
            throw new IllegalArgumentException("Invalid promotion price!");
        }
        if (promotionDTO.getProduct() == null) {
            throw new IllegalArgumentException("Product not found!");
        }
    }



}
