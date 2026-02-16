package com.example.inventory_service.service;

import com.example.inventory_service.dto.requestDtos.InventoryCreateUpdateRequest;
import com.example.inventory_service.dto.responseDtos.InventoryResponse;
import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.exception.ResourceNotFoundException;
import com.example.inventory_service.mapper.InventoryMapper;
import com.example.inventory_service.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Transactional
    public InventoryResponse createInventory(InventoryCreateUpdateRequest request) {
        if (request.productId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        if (inventoryRepository.existsByProductId(request.productId())) {
            throw new IllegalArgumentException("Inventory for Product ID '" + request.productId() + "' already exists");
        }

        Inventory inventory = inventoryMapper.toEntityInventory(request);
        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toInventoryResponse(inventory);
    }

    @Transactional
    public InventoryResponse updateInventory(UUID id, InventoryCreateUpdateRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("Inventory ID cannot be null");
        }

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));

        // Check if updating productId to one that already exists (and isn't this one)
        if (!inventory.getProductId().equals(request.productId())) {
             if (inventoryRepository.existsByProductId(request.productId())) {
            throw new IllegalArgumentException("Inventory for Product ID '" + request.productId() + "' already exists");
            }
             inventory.setProductId(request.productId());
        }

        inventory.setAvailableQuantity(request.availableQuantity());
        inventory.setReservedQuantity(request.reservedQuantity());

        inventory = inventoryRepository.save(inventory);
        return inventoryMapper.toInventoryResponse(inventory);
    }

    public InventoryResponse getInventoryById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Inventory ID cannot be null");
        }
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + id));
        return inventoryMapper.toInventoryResponse(inventory);
    }

    public InventoryResponse getInventoryByProductId(UUID productId) {
        if (productId == null) {
             throw new IllegalArgumentException("Product ID cannot be null");
        }
        Inventory inventory = inventoryRepository.findByProductId(productId)
                 .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + productId));
        return inventoryMapper.toInventoryResponse(inventory);
    }
}
