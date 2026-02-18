package com.example.inventory_service.controller;

import com.example.inventory_service.dto.requestDtos.InventoryCreateUpdateRequest;
import com.example.inventory_service.dto.responseDtos.InventoryResponse;
import com.example.inventory_service.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @RequestBody @Validated InventoryCreateUpdateRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Only ADMINs can create inventory");
        }
        InventoryResponse response = inventoryService.createInventory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @PathVariable UUID id,
            @RequestBody @Validated InventoryCreateUpdateRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Only ADMINs can update inventory");
        }
        InventoryResponse response = inventoryService.updateInventory(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getInventoryById(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @PathVariable UUID id) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMINs can view inventory details");
        }
        InventoryResponse response = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @PathVariable UUID productId) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMINs can view inventory details");
        }
        InventoryResponse response = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(response);
    }
}
