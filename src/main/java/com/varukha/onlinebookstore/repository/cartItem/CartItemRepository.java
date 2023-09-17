package com.varukha.onlinebookstore.repository.cartItem;

import com.varukha.onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
