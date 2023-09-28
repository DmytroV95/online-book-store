package com.varukha.onlinebookstore.dto.order;

import com.varukha.onlinebookstore.model.Order;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    private Long id;
    private Order.Status status;
}
