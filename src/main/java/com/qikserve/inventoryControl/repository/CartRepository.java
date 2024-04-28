package com.qikserve.inventoryControl.repository;

import com.qikserve.inventoryControl.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository  extends JpaRepository<Cart, String> {
}
