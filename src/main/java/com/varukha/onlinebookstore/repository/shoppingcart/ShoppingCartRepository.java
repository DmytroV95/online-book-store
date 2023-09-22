package com.varukha.onlinebookstore.repository.shoppingcart;

import com.varukha.onlinebookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("""
            FROM ShoppingCart sc
            LEFT JOIN FETCH sc.cartItems ci
            LEFT JOIN FETCH ci.book
            WHERE sc.user.id = :id
            """)
    Optional<ShoppingCart> findByUserId(Long id);
}
