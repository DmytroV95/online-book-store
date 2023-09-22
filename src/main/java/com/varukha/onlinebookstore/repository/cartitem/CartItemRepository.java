package com.varukha.onlinebookstore.repository.cartitem;

import com.varukha.onlinebookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findCartItemByBookId(Long id);

    void deleteAllByShoppingCartId(Long id);
}
