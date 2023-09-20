package com.varukha.onlinebookstore.repository.order;

import com.varukha.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.varukha.onlinebookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long orderId);

}
