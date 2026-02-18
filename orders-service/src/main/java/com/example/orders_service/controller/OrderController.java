package com.example.orders_service.controller;

import com.example.orders_service.dto.OrderRequest;
import com.example.orders_service.dto.OrderResponse;
import com.example.orders_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestHeader("X-User-Name") String username,
            @RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.placeOrder(orderRequest, username);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable java.util.UUID id,
            @RequestParam String status) {
        if (!"ADMIN".equals(role)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        OrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @RequestHeader("X-User-Name") String username,
            @RequestHeader("X-User-Role") String role,
            @PathVariable java.util.UUID id) {
        orderService.cancelOrder(id, username, role);
        return ResponseEntity.ok().build();
    }
}
