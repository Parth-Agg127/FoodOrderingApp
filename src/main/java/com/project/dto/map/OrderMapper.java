package com.project.dto.map;

import com.project.dto.OrderItemDTO;
import com.project.dto.OrderResponseDTO;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "subtotal", source = "subtotal")
    @Mapping(target = "discount", source = "discount")
    @Mapping(target = "gst", source = "gst")
    @Mapping(target = "totalPrice", source = "totalAmount")
    @Mapping(target = "createdAt", source = "orderDate")
    OrderResponseDTO toOrderResponseDTO(Order order);

    List<OrderResponseDTO> toOrderResponseDTOs(List<Order> orders);

    @Mapping(target = "itemId", source = "menuItem.id") // MenuItem id, not OrderItem id
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price") // Ensure you have price in DTO
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItems);
}

