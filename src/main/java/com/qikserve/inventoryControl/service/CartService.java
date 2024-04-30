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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;


    public List<CartDTO> findAll() {
        List<Cart> carts = cartRepository.findAll();
        List<CartDTO> cartDTO = new ArrayList<>();
        carts.forEach(cart -> cartDTO.add(Util.modelMapper.map(cart, CartDTO.class)));
        return cartDTO;
    }

    public CartDTO findBy(String id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            return Util.modelMapper.map(cart.get(), CartDTO.class);
        } else {
            throw new EntityNotFoundException("Cart not found!");
        }
    }

    public CartDTO insert(CartDTO cartDTO) {
        checkCart(cartDTO);
        Cart cart = Util.modelMapper.map(cartDTO, Cart.class);
        cart.setId(Util.createID());
        Cart cartCreated = cartRepository.save(cart);
        return Util.modelMapper.map(cartCreated, CartDTO.class);
    }

    public CartDTO update(CartDTO cartDTO, String id) {
        CartDTO cartToUpdate = findBy(id);
        Cart cart = new Cart();
        cart.setId(cartToUpdate.id());
        cart.setCustomer(cartDTO.customer());
        cart.setProducts(cartDTO.products());
        cart.setQuantiy(cartDTO.quantity());
        cart.setTotalPrice(cartDTO.totalPrice());
        cartRepository.save(cart);
        return Util.modelMapper.map(cart, CartDTO.class);
    }

    public void remove(String id) {
        CartDTO cartToRemove = findBy(id);
        cartRepository.delete(Util.modelMapper.map(cartToRemove, Cart.class));
    }

    private void checkCart(CartDTO cartDTO) throws IllegalArgumentException {

        if (cartDTO == null) {
            throw new IllegalArgumentException("Invalid cart!");
        }
        if (cartDTO.customer() == null) {
            throw new IllegalArgumentException("Customer not found!");
        }
    }


    public CartDTO addProductToCart(List<ProductDTO> productsDTO, String cart_id) {

        validateProducts(productsDTO);
        CartDTO cartDTO = findBy(cart_id);

        double totalSavings = 0;
        int quantity = 0;

        Cart cart = Util.modelMapper.map(cartDTO, Cart.class);

        for (ProductDTO productDTO : productsDTO) {
            Product product = Util.modelMapper.map(productDTO, Product.class);
            totalSavings = addPromotions(product, cart);
            quantity++;
        }

        cart.setQuantiy(quantity);
        cart.setTotalPrice(getTotalPrice(cart));
        cart.setTotalSavings(totalSavings);

        return Util.modelMapper.map(cart, CartDTO.class);

    }

    private double getTotalPrice(Cart cart) {

        double totalPrice = 0;
        for (Product product : cart.getProducts()) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }

    private double addPromotions(Product product, Cart cart) {

        double totalSavings = 0;

        List<PromotionDTO> promotionsDTO = promotionService.findAllByProduct(product.getId());

        if (promotionsDTO != null && promotionsDTO.size() > 0) {

            for (PromotionDTO promotionDTO : promotionsDTO) {

                List<Product> products = cart.getProducts().stream()
                        .filter(productFilter -> productFilter.getId().equals(product.getId()))
                        .collect(Collectors.toList());

                switch (promotionDTO.type()) {
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
                        throw new IllegalArgumentException("Unknown promotion type: " + promotionDTO.type());
                }
            }
        }
        return totalSavings;
    }


    private double applyBuyXGetYFreePromotion(Cart cart, PromotionDTO promotionDTO,
                                              Product product, List<Product> products) {

        double savings = 0;

        if (products.size() >= promotionDTO.required_qty() && products.size() % 2 == 0) {
            savings = ((products.size() * product.getPrice()) / 2);
            product.setPrice(0);
        } else if (products.size() >= promotionDTO.required_qty() && products.size() % 2 != 0) {
            savings = (((products.size() * product.getPrice()) / 2) + product.getPrice());
        }

        cart.getProducts().add(product);
        return savings;
    }

    private double applyQtyBasedPriceOverride(Cart cart, PromotionDTO promotionDTO,
                                              Product product, List<Product> products) {

        double totalPrice = ((products.size()+1) * product.getPrice());
        double normalPrice = product.getPrice();

        if (products.size() >= promotionDTO.required_qty() - 1) {
            double discountAmount = product.getPrice() * 0.363;
            double priceWithDiscount = product.getPrice() - discountAmount;
            product.setPrice(priceWithDiscount);
            cart.getProducts().add(product);
        }

        double newPrice = (((products.size() - 1) * product.getPrice()) + normalPrice);
        double savings = totalPrice - newPrice;
        return savings;
    }

    private double applyFlatPercentDiscount(Cart cart, Product product) {

        double saving = (product.getPrice() * 0.10);
        double newPrice = product.getPrice() - saving;

        product.setPrice(newPrice);
        cart.getProducts().add(product);

        return saving;

    }


    private void validateProducts(List<ProductDTO> productsDTO) {
        if (productsDTO == null || productsDTO.isEmpty()) {
            throw new IllegalArgumentException("Invalid product!");
        }
        productsDTO.forEach(productDTO -> {
            productService.checkProduct(productDTO);
        });
    }




}
