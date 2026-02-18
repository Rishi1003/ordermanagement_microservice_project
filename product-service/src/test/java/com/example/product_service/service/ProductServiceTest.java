package com.example.product_service.service;

import com.example.product_service.client.InventoryClient;
import com.example.product_service.dto.requestDtos.InventoryRequest;
import com.example.product_service.dto.requestDtos.ProductCreateUpdateRequest;
import com.example.product_service.dto.responseDtos.ProductResponse;
import com.example.product_service.entity.Category;
import com.example.product_service.entity.Product;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldCreateProduct() {
        UUID categoryId = UUID.randomUUID();
        ProductCreateUpdateRequest productRequest = new ProductCreateUpdateRequest(
                "Test Product", "Description", BigDecimal.valueOf(100.0), categoryId, "TEST-SKU");

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setCategory(category);
        product.setSku("TEST-SKU");

        ProductResponse productResponse = new ProductResponse(
                productId, "Test Product", "Description", BigDecimal.valueOf(100.0), null, categoryId, "Electronics",
                "TEST-SKU", null, null);

        when(productMapper.toEntityProduct(productRequest)).thenReturn(product);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.existsBySku("TEST-SKU")).thenReturn(false);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductResponse(product)).thenReturn(productResponse);

        ProductResponse savedProduct = productService.createProduct(productRequest);

        assertThat(savedProduct.id()).isEqualTo(productId);
        assertThat(savedProduct.name()).isEqualTo("Test Product");
        assertThat(savedProduct.categoryId()).isEqualTo(categoryId);

        verify(inventoryClient, times(1)).createInventory(any(InventoryRequest.class));
    }
}
