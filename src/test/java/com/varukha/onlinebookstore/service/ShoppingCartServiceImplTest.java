package com.varukha.onlinebookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.varukha.onlinebookstore.dto.book.request.CreateBookRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.CartItemDto;
import com.varukha.onlinebookstore.dto.cartitem.CreateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.varukha.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.varukha.onlinebookstore.mapper.CartItemMapper;
import com.varukha.onlinebookstore.mapper.ShoppingCartMapper;
import com.varukha.onlinebookstore.model.Book;
import com.varukha.onlinebookstore.model.CartItem;
import com.varukha.onlinebookstore.model.Category;
import com.varukha.onlinebookstore.model.ShoppingCart;
import com.varukha.onlinebookstore.model.User;
import com.varukha.onlinebookstore.repository.cartitem.CartItemRepository;
import com.varukha.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.varukha.onlinebookstore.service.shoppingcart.impl.ShoppingCartServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
    private static final User USER = new User();
    private static final Book VALID_BOOK_1 = new Book();
    private static final Category VALID_CATEGORY_1 = new Category();
    private static final ShoppingCart SHOPPING_CART = new ShoppingCart();
    private static final CartItem CART_ITEM = new CartItem();
    private static final CartItem UPDATED_CART_ITEM = new CartItem();
    private static final CartItemDto CART_ITEM_RESPONSE_DTO
            = new CartItemDto();
    private static final ShoppingCartDto SHOPPING_CART_RESPONSE_DTO
            = new ShoppingCartDto();
    private static final CreateBookRequestDto REQUEST_DTO_BOOK
            = new CreateBookRequestDto();
    private static final CreateCartItemRequestDto CART_ITEM_REQUEST_DTO
            = new CreateCartItemRequestDto();
    private static final UpdateCartItemRequestDto UPDATE_CART_ITEM_REQUEST_DTO
            = new UpdateCartItemRequestDto();
    private static final CartItemDto UPDATED_CART_ITEM_RESPONSE_DTO
            = new CartItemDto();

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

    @BeforeEach
    void setUp() {
        USER.setId(1L);
        USER.setEmail("alice@gmail.com");
        USER.setPassword("Password");
        USER.setFirstName("Alice");
        USER.setLastName("Brown");
        USER.setShippingAddress("15 Main St, City, Country");
        USER.setDeleted(false);

        VALID_CATEGORY_1.setId(1L);
        VALID_CATEGORY_1.setName("Science Fiction");
        VALID_CATEGORY_1.setDescription("Science Fiction category description");

        VALID_BOOK_1.setId(1L);
        VALID_BOOK_1.setTitle(REQUEST_DTO_BOOK.getTitle());
        VALID_BOOK_1.setAuthor(REQUEST_DTO_BOOK.getAuthor());
        VALID_BOOK_1.setIsbn(REQUEST_DTO_BOOK.getIsbn());
        VALID_BOOK_1.setPrice(REQUEST_DTO_BOOK.getPrice());
        VALID_BOOK_1.setDescription(REQUEST_DTO_BOOK.getDescription());
        VALID_BOOK_1.setCoverImage(REQUEST_DTO_BOOK.getCoverImage());
        VALID_BOOK_1.setCategories(Set.of(VALID_CATEGORY_1));

        REQUEST_DTO_BOOK.setTitle("Sample Book Title 1");
        REQUEST_DTO_BOOK.setAuthor("Author 1");
        REQUEST_DTO_BOOK.setIsbn("978-1234567891");
        REQUEST_DTO_BOOK.setPrice(BigDecimal.valueOf(25));
        REQUEST_DTO_BOOK.setDescription("Description 1");
        REQUEST_DTO_BOOK.setCoverImage("https://example.com/book-cover1.jpg");
        REQUEST_DTO_BOOK.setCategoriesId(Set.of(VALID_CATEGORY_1.getId()));

        CART_ITEM_REQUEST_DTO.setBookId(VALID_BOOK_1.getId());
        CART_ITEM_REQUEST_DTO.setQuantity(2);

        CART_ITEM.setId(CART_ITEM_REQUEST_DTO.getBookId());
        CART_ITEM.setShoppingCart(SHOPPING_CART);
        CART_ITEM.setBook(VALID_BOOK_1);
        CART_ITEM.setQuantity(CART_ITEM_REQUEST_DTO.getQuantity());

        CART_ITEM_RESPONSE_DTO.setId(VALID_BOOK_1.getId());
        CART_ITEM_RESPONSE_DTO.setBookId(VALID_BOOK_1.getId());
        CART_ITEM_RESPONSE_DTO.setBookTitle(VALID_BOOK_1.getTitle());
        CART_ITEM_RESPONSE_DTO.setQuantity(CART_ITEM_REQUEST_DTO.getQuantity());

        SHOPPING_CART.setId(USER.getId());
        SHOPPING_CART.setUser(USER);

        SHOPPING_CART_RESPONSE_DTO.setId(1L);
        SHOPPING_CART_RESPONSE_DTO.setUserId(USER.getId());
        SHOPPING_CART_RESPONSE_DTO.setCartItems(Set.of(CART_ITEM_RESPONSE_DTO));

        UPDATE_CART_ITEM_REQUEST_DTO.setQuantity(5);

        UPDATED_CART_ITEM_RESPONSE_DTO.setId(VALID_BOOK_1.getId());
        UPDATED_CART_ITEM_RESPONSE_DTO.setBookId(VALID_BOOK_1.getId());
        UPDATED_CART_ITEM_RESPONSE_DTO.setBookTitle(VALID_BOOK_1.getTitle());
        UPDATED_CART_ITEM_RESPONSE_DTO.setQuantity(UPDATE_CART_ITEM_REQUEST_DTO.getQuantity());

        UPDATED_CART_ITEM.setId(UPDATED_CART_ITEM_RESPONSE_DTO.getBookId());
        UPDATED_CART_ITEM.setShoppingCart(SHOPPING_CART);
        UPDATED_CART_ITEM.setBook(VALID_BOOK_1);
        UPDATED_CART_ITEM.setQuantity(UPDATED_CART_ITEM_RESPONSE_DTO.getQuantity());
    }

    @Test
    @DisplayName("""
            Test the 'createShoppingCartForUser' method
            to create shopping cart after user registration
            """)
    void createShoppingCartForUser_ValidUserData_ReturnVoid() {
        when(shoppingCartRepository.save(argThat(
                savedCart -> savedCart.getUser().equals(USER))))
                .thenReturn(SHOPPING_CART);

        shoppingCartService.createShoppingCartForUser(USER);

        verify(shoppingCartRepository, times(1))
                .save(argThat(savedCart -> savedCart.getUser().equals(USER)));
    }

    @Test
    @DisplayName("""
            Test the 'createCartItem' method to create
            new cart item in shopping cart or update quantity
            of existed cart item in shopping cart
            """)
    void createCartItem_ValidCreateCartItemRequestDtoData_ReturnCartItemDto() {

        when(cartItemMapper.toDto(CART_ITEM)).thenReturn(CART_ITEM_RESPONSE_DTO);
        when(cartItemRepository.findCartItemByBookId(CART_ITEM_REQUEST_DTO.getBookId()))
                .thenReturn(Optional.of(CART_ITEM));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(CART_ITEM);

        CartItemDto result = shoppingCartService.createCartItem(CART_ITEM_REQUEST_DTO);

        assertNotNull(result);
        assertEquals(CART_ITEM_RESPONSE_DTO.getBookId(), result.getBookId());
        assertEquals(CART_ITEM_RESPONSE_DTO.getQuantity(), result.getQuantity());
    }

    @Test
    @DisplayName("""
            Test the 'getShoppingCartByUserId' method to retrieve
            shopping cart by user ID
            """)
    void getShoppingCart_ReturnShoppingCartDto() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(USER);
        when(shoppingCartRepository.findByUserId(USER.getId()))
                .thenReturn(Optional.of(SHOPPING_CART));
        when(shoppingCartMapper.toDto(SHOPPING_CART)).thenReturn(SHOPPING_CART_RESPONSE_DTO);
        ShoppingCartDto result = shoppingCartService.getShoppingCart();

        assertNotNull(result);
        verify(shoppingCartRepository).findByUserId(USER.getId());
    }

    @Test
    @DisplayName("""
            Test the 'deleteById' method to delete the cart item
            from shopping cart by shopping cart ID
            """)
    void deleteById_ValidId_ReturnVoid() {
        shoppingCartService.deleteById(SHOPPING_CART.getId());
        verify(cartItemRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("""
            Test the 'updateCartItemQuantity' method to update the cart item
            quantity in shopping cart
            """)
    void updateCartItemQuantity_ValidIdAndUpdateRequestDto_ReturnCartItemDto() {
        Long cartItemId = CART_ITEM.getId();
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(CART_ITEM));
        when(cartItemRepository.save(CART_ITEM)).thenReturn(UPDATED_CART_ITEM);
        when(cartItemMapper.toDto(UPDATED_CART_ITEM)).thenReturn(UPDATED_CART_ITEM_RESPONSE_DTO);

        CartItemDto result = shoppingCartService
                .updateCartItemQuantity(cartItemId, UPDATE_CART_ITEM_REQUEST_DTO);

        assertNotNull(result);
        assertEquals(UPDATED_CART_ITEM_RESPONSE_DTO.getQuantity(), result.getQuantity());
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
}
