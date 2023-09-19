package com.varukha.onlinebookstore.repository.order;

import com.varukha.onlinebookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
