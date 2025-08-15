package com.project.dto;

import com.project.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private OrderStatus status;
    private double subtotal;
    private double discount;
    private double gst;
    private double totalPrice;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;



}
