package com.varukha.onlinebookstore.service.cartItem;

import com.varukha.onlinebookstore.dto.cartItem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartItem.CreateCartItemRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto);

    List<CartItemDto> getAll(Pageable pageable);

    void deleteById(Long id);

}
