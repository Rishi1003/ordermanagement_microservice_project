package com.example.product_service.integration;

import com.example.product_service.client.InventoryClient;
import com.example.product_service.dto.requestDtos.InventoryRequest;
import com.example.product_service.dto.requestDtos.ProductCreateUpdateRequest;
import com.example.product_service.dto.responseDtos.ProductResponse;
import com.example.product_service.entity.Category;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import com.example.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("test")
@org.springframework.test.context.TestPropertySource(locations = "classpath:product-service-test.yml", properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.cloud.config.enabled=false"
})
@Transactional
public class ProductIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private InventoryClient inventoryClient;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldCreateProductAndPersistInDatabase() {
        // Given
        Category category = new Category();
        category.setName("Electronics");
        category = categoryRepository.save(category);

        ProductCreateUpdateRequest request = new ProductCreateUpdateRequest(
                "Integration Test Product",
                "Description",
                BigDecimal.valueOf(150.0),
                category.getId(),
                "INT-TEST-SKU");

        doNothing().when(inventoryClient).createInventory(any(InventoryRequest.class));

        // When
        ProductResponse response = productService.createProduct(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("Integration Test Product");

        // Verify Database
        assertThat(productRepository.findById(response.id())).isPresent();
        assertThat(productRepository.existsBySku("INT-TEST-SKU")).isTrue();
    }

    @Test
    void shouldFailToCreateProductWithInvalidData() {
        // Given
        ProductCreateUpdateRequest request = new ProductCreateUpdateRequest(
                "", // Invalid name
                "Description",
                BigDecimal.valueOf(-10.0), // Invalid price
                UUID.randomUUID(),
                ""); // Invalid SKU

        // When/Then
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            productService.createProduct(request);
            productRepository.flush(); // Force flush to trigger JPA validation
        });
    }
}
