package com.varukha.onlinebookstore.repository.orderitem;

import com.varukha.onlinebookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
