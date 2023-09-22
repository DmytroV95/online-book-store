package com.varukha.onlinebookstore.repository.order;

import com.varukha.onlinebookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long orderId);

    @Query("FROM Order o LEFT JOIN FETCH o.orderItems")
    List<Order> findAllOrders(Pageable pageable);

}
