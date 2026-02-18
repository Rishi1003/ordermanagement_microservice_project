package com.example.product_service.controller;

import com.example.product_service.dto.requestDtos.CategoryCreateRequest;
import com.example.product_service.dto.requestDtos.CategoryUpdateRequest;
import com.example.product_service.dto.responseDtos.CategoryResponse;
import com.example.product_service.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @Valid @RequestBody CategoryCreateRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Only ADMINs can create categories");
        }
        CategoryResponse created = categoryService.createCategory(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.categoryId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Only ADMINs can update categories");
        }
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        CategoryResponse updated = categoryService.updateCategory(request, id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<CategoryResponse> deactivateCategory(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @PathVariable UUID id) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMINs can deactivate categories");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        CategoryResponse deactivated = categoryService.deactivateCategory(id);
        return ResponseEntity.ok(deactivated);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<CategoryResponse> activateCategory(
            @RequestHeader(value = "X-User-Role", required = true) String role,
            @PathVariable UUID id) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMINs can activate categories");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }

        CategoryResponse activated = categoryService.activateCategory(id);
        return ResponseEntity.ok(activated);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
        List<CategoryResponse> activeCategories = categoryService.getActiveCategories();
        return ResponseEntity.ok(activeCategories);
    }

}
