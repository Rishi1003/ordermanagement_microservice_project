package com.example.orders_service.service;

import com.example.orders_service.dto.OrderRequest;
import com.example.orders_service.dto.OrderResponse;
import com.example.orders_service.entity.Order;
import com.example.orders_service.mapper.OrderMapper;
import com.example.orders_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = orderMapper.toEntityOrder(orderRequest);
        
        // Ensure bidirectional relationship is set for JPA cascading
        if (order.getOrderLineItems() != null) {
            order.getOrderLineItems().forEach(item -> item.setOrder(order));
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderResponse(savedOrder);
    }
}
