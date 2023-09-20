package com.varukha.onlinebookstore.service.shoppingcart.impl;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.mapper.CartItemMapper;
import com.varukha.onlinebookstore.mapper.ShoppingCartMapper;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.book.BookRepository;
import com.varukha.onlinebookstore.repository.cartitem.CartItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.varukha.onlinebookstore.service.shoppingcart.ShoppingCartService;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public void create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem existedCartItem = getCartItemByBookId(requestDto.getBookId());
        if (existedCartItem == null || !existedCartItem.getBook().getId()
                .equals(requestDto.getBookId())) {
            return createNewCartItem(requestDto);
        } else {
            return updateCartItemQuantityWhenSave(existedCartItem, requestDto.getQuantity());
        }
    }

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long id) {
        return shoppingCartRepository.findByUserId(getUserId())
                .map(shoppingCartMapper::toDto)
                .orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    @Override
    public CartItemDto updateCartItemQuantity(Long id,
                                              UpdateCartItemRequestDto cartItemRequestDto) {
        CartItem cartItemById = getCartItemById(id);
        int requestedQuantity = cartItemRequestDto.getQuantity();
        cartItemById.setQuantity(requestedQuantity);
        CartItem savedCartItem = cartItemRepository.save(cartItemById);
        return cartItemMapper.toDto(savedCartItem);
    }

    private CartItemDto updateCartItemQuantityWhenSave(CartItem existingCartItem, int quantity) {
        existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        CartItem savedCartItem = cartItemRepository.save(existingCartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    private CartItemDto createNewCartItem(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCartRepository
                .findByUserId(getUserId())
                .orElseThrow());
        cartItem.setBook(bookRepository
                .findById(requestDto.getBookId()).orElseThrow());
        cartItem.setQuantity(requestDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    private CartItem getCartItemByBookId(Long id) {
        return cartItemRepository.findCartItemByBookId(id).orElse(null);
    }
}
