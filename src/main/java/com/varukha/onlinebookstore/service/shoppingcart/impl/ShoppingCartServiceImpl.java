package com.varukha.onlinebookstore.service.shoppingcart.impl;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.exception.EntityNotFoundException;
import com.varukha.onlinebookstore.mapper.CartItemMapper;
import com.varukha.onlinebookstore.mapper.ShoppingCartMapper;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.book.BookRepository;
import com.varukha.onlinebookstore.repository.cartitem.CartItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.varukha.onlinebookstore.service.shoppingcart.ShoppingCartService;
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
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    @Override
    public CartItemDto createCartItem(CreateCartItemRequestDto requestDto) {
        Optional<CartItem> existingCartItem = getCartItemByBookId(requestDto.getBookId());
        if (existingCartItem.isPresent()) {
            return updateCartItemQuantity(existingCartItem.get(), requestDto.getQuantity());
        } else {
            return createNewCartItem(requestDto);
        }
    }

    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartRepository.findByUserId(getAuthenticatedUserId())
                .map(shoppingCartMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart by user id: "
                        + getAuthenticatedUserId() + " not found"));
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
        cartItemById.setQuantity(cartItemRequestDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItemById);
        return cartItemMapper.toDto(savedCartItem);
    }

    private CartItemDto updateCartItemQuantity(CartItem existingCartItem, Integer quantity) {
        existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        CartItem savedCartItem = cartItemRepository.save(existingCartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public void clearShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart by id: "
                        + userId + " not found"));
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
    }

    private CartItemDto createNewCartItem(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCartRepository
                .findByUserId(getAuthenticatedUserId())
                .orElseThrow());
        cartItem.setBook(bookRepository
                .findById(requestDto.getBookId()).orElseThrow());
        cartItem.setQuantity(requestDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    private Optional<CartItem> getCartItemByBookId(Long id) {
        return cartItemRepository.findCartItemByBookId(id);
    }

    private CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CartItem by id: "
                        + id + " not found"));
    }
}
