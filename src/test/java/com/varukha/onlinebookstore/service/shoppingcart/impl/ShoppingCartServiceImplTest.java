package com.varukha.onlinebookstore.service.shoppingcart.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.mapper.CartItemMapper;
import com.varukha.onlinebookstore.mapper.ShoppingCartMapper;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.cartitem.CartItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("""
            Test the 'createShoppingCartForUser' method
            to create shopping cart after user registration
            """)
    void createShoppingCartForUser_ValidUserData_ReturnVoid() {
        User user = getUser();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        when(shoppingCartRepository.save(argThat(
                savedCart -> savedCart.getUser()
                        .equals(user))))
                .thenReturn(shoppingCart);
        shoppingCartService.createShoppingCartForUser(user);
        verify(shoppingCartRepository, times(1))
                .save(argThat(savedCart -> savedCart.getUser().equals(user)));
    }

    @Test
    @DisplayName("""
            Test the 'createCartItem' method to create
            new cart item in shopping cart or update quantity
            of existed cart item in shopping cart
            """)
    void createCartItem_ValidCreateCartItemRequestDtoData_ReturnCartItemDto() {
        Book book = getBook();
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(1L);
        requestDto.setQuantity(2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCart);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(book.getId());
        cartItemDto.setBookId(requestDto.getBookId());
        cartItemDto.setBookTitle(book.getTitle());
        cartItemDto.setQuantity(requestDto.getQuantity());

        when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);
        when(cartItemRepository.findCartItemByBookId(requestDto.getBookId())).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItemDto result = shoppingCartService.createCartItem(requestDto);

        verify(cartItemRepository).findCartItemByBookId(requestDto.getBookId());
        verify(cartItemRepository).save(any(CartItem.class));

        assertNotNull(result);
        assertEquals(requestDto.getBookId(), result.getBookId());
        assertEquals(requestDto.getQuantity(), result.getQuantity());
    }

    @Test
    @DisplayName("""
            Test the 'getShoppingCartByUserId' method to retrieve
            shopping cart by user ID
            """)
    void getShoppingCartByUserId_ExistingUserId_ReturnShoppingCartDto() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(getUser());
        shoppingCart.setCartItems(Set.of(new CartItem()));

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(getUser().getId());
        shoppingCartDto.setCartItems(Set.of(new CartItemDto()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(getUser());
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);
        ShoppingCartDto result = shoppingCartService.getShoppingCartByUserId(getUser().getId());

        verify(shoppingCartRepository).findByUserId(anyLong());
        assertNotNull(result);
    }

    @Test
    @DisplayName("""
            Test the 'deleteById' method to delete the cart item
            from shopping cart by shopping cart ID
            """)
    void deleteById_ValidId_ReturnVoid() {
        shoppingCartService.deleteById(1L);
        verify(cartItemRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("""
            Test the 'updateCartItemQuantity' method to update the cart item
            quantity in shopping cart
            """)
    void updateCartItemQuantity_ValidIdAndUpdateRequestDto_ReturnCartItemDto() {
        Long cartItemId = 1L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto();
        requestDto.setQuantity(5);

        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(cartItemId);
        existingCartItem.setQuantity(1);

        CartItem savedCartItem = new CartItem();
        savedCartItem.setId(cartItemId);
        savedCartItem.setQuantity(requestDto.getQuantity());

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItemId);
        cartItemDto.setBookId(getBook().getId());
        cartItemDto.setBookTitle(getBook().getTitle());
        cartItemDto.setQuantity(requestDto.getQuantity());

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(existingCartItem)).thenReturn(savedCartItem);
        when(cartItemMapper.toDto(savedCartItem)).thenReturn(cartItemDto);

        CartItemDto result = shoppingCartService.updateCartItemQuantity(cartItemId, requestDto);
        assertNotNull(result);
        assertEquals(requestDto.getQuantity(), result.getQuantity());
    }

    @Test
    @DisplayName("""
            Test the 'clearShoppingCart' method to clear
            the shopping cart after an order is created
            """)
    void clearShoppingCart_ExistingUserId_ReturnVoid() {
        when(shoppingCartRepository.findByUserId(anyLong()))
                .thenReturn(Optional.of(new ShoppingCart()));
        shoppingCartService.clearShoppingCart(1L);
        verify(shoppingCartRepository).findByUserId(anyLong());
        verify(cartItemRepository).deleteAll(any());
    }

    @NotNull
    private static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("alice@gmail.com");
        user.setPassword("Password");
        user.setFirstName("Alice");
        user.setLastName("Brown");
        user.setShippingAddress("15 Main St, City, Country");
        user.setDeleted(false);
        return user;
    }

    @NotNull
    private static Book getBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book Title");
        book.setAuthor("John Doe");
        book.setIsbn("978-1234567890");
        book.setPrice(BigDecimal.valueOf(20));
        book.setDescription("This is a sample book description");
        book.setCoverImage("https://example.com/book-cover.jpg");
        return book;
    }
}
