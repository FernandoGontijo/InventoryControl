package com.qikserve.inventoryControl.service;


import com.qikserve.inventoryControl.dto.CartDTO;
import com.qikserve.inventoryControl.dto.ProductDTO;
import com.qikserve.inventoryControl.dto.PromotionDTO;
import com.qikserve.inventoryControl.model.Cart;
import com.qikserve.inventoryControl.model.Product;
import com.qikserve.inventoryControl.repository.CartRepository;
import com.qikserve.inventoryControl.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final Logger logger = LogManager.getLogger(CartService.class);


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;


    public List<CartDTO> findAll() {
        logger.debug("Finding all carts");
        List<Cart> carts = cartRepository.findAll();
        List<CartDTO> cartDTO = new ArrayList<>();
        carts.forEach(cart -> cartDTO.add(Util.modelMapper.map(cart, CartDTO.class)));
        return cartDTO;
    }

    public CartDTO findBy(String id) {
        logger.debug("Finding cart by ID: {}", id);
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            return Util.modelMapper.map(cart, CartDTO.class);
        } else {
            logger.error("Cart not found with ID: {}", id);
            throw new EntityNotFoundException("Cart not found!");
        }
    }

    public CartDTO insert(CartDTO cartDTO) {
        logger.debug("Inserting cart: {}", cartDTO);
        checkCart(cartDTO);
        Cart cart = Util.modelMapper.map(cartDTO, Cart.class);
        cart.setId(Util.createID());
        Cart cartCreated = cartRepository.save(cart);
        return Util.modelMapper.map(cartCreated, CartDTO.class);
    }

    public CartDTO update(CartDTO cartDTO, String id) {
        logger.debug("Updating cart with ID: {}", id);
        CartDTO cartToUpdate = findBy(id);
        Cart cart = new Cart();
        cart.setId(cartToUpdate.getId());
        cart.setCustomer(cartDTO.getCustomer());
        cart.setProducts(cartDTO.getProducts());
        cart.setQuantity(cartDTO.getQuantity());
        cart.setTotalPrice(cartDTO.getTotalPrice());
        cartRepository.save(cart);
        return Util.modelMapper.map(cart, CartDTO.class);
    }

    public void remove(String id) {
        logger.debug("Removing cart with ID: {}", id);
        CartDTO cartToRemove = findBy(id);
        cartRepository.delete(Util.modelMapper.map(cartToRemove, Cart.class));
    }

    private void checkCart(CartDTO cartDTO) throws IllegalArgumentException {

        if (cartDTO == null) {
            throw new IllegalArgumentException("Invalid cart!");
        }
        if (cartDTO.getCustomer() == null) {
            throw new IllegalArgumentException("Customer not found!");
        }
    }


    public CartDTO addProductToCart(List<ProductDTO> productsDTO, String cart_id) {

        logger.debug("Adding products to cart with ID: {}", cart_id);
        validateProducts(productsDTO);
        CartDTO cartDTO = findBy(cart_id);

        int totalSavings = 0;
        int quantity = 0;

        Cart cart = Util.modelMapper.map(cartDTO, Cart.class);

        for (ProductDTO productDTO : productsDTO) {
            Product product = Util.modelMapper.map(productDTO, Product.class);
            totalSavings += addPromotions(product, cart);
            quantity++;
        }

        cart.setQuantity(quantity);
        cart.setTotalPrice(getTotalPrice(cart));
        cart.setTotalSavings(totalSavings);

        return Util.modelMapper.map(cart, CartDTO.class);

    }

    private int getTotalPrice(Cart cart) {
        logger.debug("Getting the total price cart.");
        int totalPrice = 0;
        for (Product product : cart.getProducts()) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    private int addPromotions(Product product, Cart cart) {
        logger.debug("Adding promotions.");
        int totalSavings = 0;

        List<PromotionDTO> promotionsDTO = promotionService.findAllByProduct(product.getId());

        if (promotionsDTO != null && promotionsDTO.size() > 0) {

            for (PromotionDTO promotionDTO : promotionsDTO) {

                List<Product> products = cart.getProducts().stream()
                        .filter(productFilter -> productFilter.getId().equals(product.getId()))
                        .collect(Collectors.toList());

                switch (promotionDTO.getType()) {
                    case "BUY_X_GET_Y_FREE":
                        totalSavings += applyBuyXGetYFreePromotion(cart, promotionDTO, product, products);
                        break;
                    case "QTY_BASED_PRICE_OVERRIDE":
                        totalSavings += applyQtyBasedPriceOverride(cart, promotionDTO, product, products);
                        break;
                    case "FLAT_PERCENT":
                        totalSavings += applyFlatPercentDiscount(cart, product);
                        break;
                    default:
                        cart.getProducts().add(product);
                }
            }
        } else {
            cart.getProducts().add(product);
        }
        return totalSavings;
    }


    private int applyBuyXGetYFreePromotion(Cart cart, PromotionDTO promotionDTO,
                                              Product product, List<Product> products) {
        logger.debug("Applying 'Buy X Get Y Free' promotion for product {} in cart {}",
                product.getName(), cart.getId());

        int savings = 0;

        if ((products.size() + 1) >= promotionDTO.getAmount() && (products.size() + 1) % 2 == 0) {
            savings = (((products.size() + 1) * product.getPrice()) / 2);
            product.setPrice(0);
        } else if ((products.size() + 1) >= promotionDTO.getAmount() && (products.size() + 1) % 2 != 0) {
            savings = ((((products.size() + 1) * product.getPrice()) / 2) + product.getPrice());
        }

        cart.getProducts().add(product);
        return savings;
    }

    private int applyQtyBasedPriceOverride(Cart cart, PromotionDTO promotionDTO,
                                              Product product, List<Product> products) {

        logger.debug("Applying 'Qty Based Price Override' promotion for product {} in cart {}",
                product.getName(), cart.getId());
        int savings = 0;
        int totalPrice = ((products.size()+1) * product.getPrice());

        int normalPrice = 0;

        for (Product prod : products) {
            normalPrice += prod.getPrice();
        }


        if (products.size()  >= promotionDTO.getRequiredQty() - 1) {
            double discountAmount = product.getPrice() * 0.363;
            int priceWithDiscount = (int) (product.getPrice() - discountAmount);
            product.setPrice(priceWithDiscount);
            savings = totalPrice - (priceWithDiscount + normalPrice);
        }

        cart.getProducts().add(product);

        return savings;
    }

    private int applyFlatPercentDiscount(Cart cart, Product product) {

        logger.debug("Applying 'Flat Percent Discount' promotion for product {} in cart {}",
                product.getName(), cart.getId());

        double saving = product.getPrice() * 0.10;
        int newPrice = (int) Math.ceil(product.getPrice() - saving);

        product.setPrice(newPrice);
        cart.getProducts().add(product);

        return (int) Math.ceil(saving);
    }



    private void validateProducts(List<ProductDTO> productsDTO) {
        logger.debug("Validate products");
        if (productsDTO == null || productsDTO.isEmpty()) {
            throw new IllegalArgumentException("Invalid product!");
        }
        productsDTO.forEach(productDTO -> {
            productService.checkProduct(productDTO);
        });
    }




}
