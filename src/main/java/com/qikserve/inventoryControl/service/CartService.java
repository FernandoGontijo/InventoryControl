package com.qikserve.inventoryControl.service;


import com.qikserve.inventoryControl.dto.CartDTO;
import com.qikserve.inventoryControl.model.Cart;
import com.qikserve.inventoryControl.repository.CartRepository;
import com.qikserve.inventoryControl.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;


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
        checkProduct(cartDTO);
        Cart cart = Util.modelMapper.map(cartDTO, Cart.class);
        cart.setId(Util.createID());
        Cart cartCreated = cartRepository.save(cart);
        return Util.modelMapper.map(cartCreated, CartDTO.class);
    }

    public CartDTO update(CartDTO cartDTO) {
        CartDTO cartToUpdate = findBy(cartDTO.id());
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

    private void checkProduct(CartDTO cartDTO) throws IllegalArgumentException {

        if (cartDTO == null) {
            throw new IllegalArgumentException("Invalid cart!");
        }
        if (cartDTO.customer() == null) {
            throw new IllegalArgumentException("Customer not fund!");
        }
    }




}
