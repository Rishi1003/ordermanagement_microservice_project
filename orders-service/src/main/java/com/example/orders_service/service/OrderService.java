package com.example.orders_service.service;

import com.example.orders_service.dto.OrderRequest;
import com.example.orders_service.dto.OrderResponse;
import com.example.orders_service.entity.Order;
import com.example.orders_service.mapper.OrderMapper;
import com.example.orders_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.orders_service.dto.InventoryResponse;
import com.example.orders_service.dto.InventoryCreateUpdateRequest;
import com.example.orders_service.dto.ProductResponse;
import com.example.orders_service.dto.UserResponse;
import com.example.orders_service.client.ProductClient;
import com.example.orders_service.client.InventoryClient;
import com.example.orders_service.client.UserClient;
import com.example.orders_service.entity.OrderLineItems;
import java.util.UUID;
import java.math.BigDecimal;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final UserClient userClient;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper,
            ProductClient productClient,
            InventoryClient inventoryClient,
            UserClient userClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
        this.userClient = userClient;
    }

    public OrderResponse placeOrder(OrderRequest orderRequest, String username) {
        Order order = orderMapper.toEntityOrder(orderRequest);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (com.example.orders_service.entity.OrderLineItems item : order.getOrderLineItems()) {

            ProductResponse product = productClient.getProductById(item.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }

            item.setPrice(product.price());

            BigDecimal lineTotal = product.price().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(lineTotal);

            InventoryResponse inventory = inventoryClient.getInventoryByProductId(item.getProductId());
            if (inventory == null || inventory.availableQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient inventory for product: " + item.getProductId());
            }
        }

        order.setTotalPrice(totalAmount);
        order.setStatus("PENDING");

        if (order.getOrderLineItems() != null) {
            order.getOrderLineItems().forEach(item -> item.setOrder(order));
        }

        Order savedOrder = orderRepository.save(order);

        try {
            for (OrderLineItems item : savedOrder.getOrderLineItems()) {
                InventoryResponse inventory = inventoryClient.getInventoryByProductId(item.getProductId());
                int newAvailable = inventory.availableQuantity() - item.getQuantity();

                InventoryCreateUpdateRequest updateRequest = new InventoryCreateUpdateRequest(
                        item.getProductId(),
                        newAvailable,
                        inventory.reservedQuantity());

                inventoryClient.updateInventory(inventory.id(), updateRequest);
            }

            savedOrder.setStatus("CONFIRMED");
            orderRepository.save(savedOrder);

        } catch (Exception e) {

            savedOrder.setStatus("FAILED");
            orderRepository.save(savedOrder);
            throw new RuntimeException("Failed to update inventory, order failed", e);
        }

        return orderMapper.toOrderResponse(savedOrder);
    }

    public OrderResponse updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        return orderMapper.toOrderResponse(saved);
    }

    public void cancelOrder(java.util.UUID orderId, String username, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!"ADMIN".equals(role)) {

            UserResponse user = userClient.getUserByEmail(username);
            if (!order.getUserId().equals(user.id())) {
                throw new RuntimeException("Access Denied: You can only cancel your own orders");
            }
        }

        if ("CANCELLED".equals(order.getStatus())) {
            throw new RuntimeException("Order is already cancelled");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);

        for (OrderLineItems item : order.getOrderLineItems()) {
            try {
                InventoryResponse inventory = inventoryClient.getInventoryByProductId(item.getProductId());
                int newAvailable = inventory.availableQuantity() + item.getQuantity();

                InventoryCreateUpdateRequest updateRequest = new InventoryCreateUpdateRequest(
                        item.getProductId(),
                        newAvailable,
                        inventory.reservedQuantity());
                inventoryClient.updateInventory(inventory.id(), updateRequest);
            } catch (Exception e) {

                System.err.println("Failed to restore inventory for product " + item.getProductId());
            }
        }
    }
}
