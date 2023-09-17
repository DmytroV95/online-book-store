package com.varukha.onlinebookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_item")
public class CartItem {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id",
            nullable = false)
    private ShoppingCart shoppingCart;

    @OneToOne
    @MapsId
    private Book book;

    private int quantity;



    public CartItem(Long id) {
        this.id = id;
    }

    public CartItem() {

    }
}
