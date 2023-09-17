package com.varukha.onlinebookstore.service.cartItem.impl;

import com.varukha.onlinebookstore.dto.cartItem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartItem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.mapper.CartItemMapper;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.repository.cartItem.CartItemRepository;
import com.varukha.onlinebookstore.service.cartItem.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(savedCartItem);
    }

    @Override
    public List<CartItemDto> getAll(Pageable pageable) {
        return cartItemRepository.findAll(pageable).stream()
                .map(cartItemMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
