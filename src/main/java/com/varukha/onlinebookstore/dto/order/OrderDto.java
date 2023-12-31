package com.varukha.onlinebookstore.dto.order;

import com.varukha.onlinebookstore.dto.orderitem.OrderItemDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    @NotBlank
    private Long id;
    @NotBlank
    private Long userId;
    @NotBlank
    private Set<OrderItemDto> orderItems;
    @NotBlank
    private String orderDate;
    @NotBlank
    @Min(0)
    private BigDecimal total;
    @NotBlank
    private String status;
}
