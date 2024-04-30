package com.qikserve.inventoryControl.controller;

import com.qikserve.inventoryControl.dto.CartDTO;
import com.qikserve.inventoryControl.dto.ProductDTO;
import com.qikserve.inventoryControl.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> carts = cartService.findAll();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable String id) {
        CartDTO cart = cartService.findBy(id);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO cartDTO) {
        CartDTO createdCart = cartService.insert(cartDTO);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable String id, @RequestBody CartDTO cartDTO) {
        CartDTO updatedCart = cartService.update(cartDTO, id);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cart_id}/addProduct")
    public ResponseEntity<CartDTO> addProductToCart(@RequestBody List<ProductDTO> productsDTO,
                                                    @PathVariable String cart_id) {
        CartDTO updatedCart = cartService.addProductToCart(productsDTO, cart_id);
        return ResponseEntity.ok(updatedCart);
    }
}
