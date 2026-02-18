package com.example.product_service.repository;

import com.example.product_service.entity.Category;
import com.example.product_service.entity.Product;
import com.example.product_service.entity.ProductStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@org.springframework.test.context.TestPropertySource(locations = "classpath:product-service-test.yml")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldSaveProduct() {
        Category category = Category.builder()
                .name("Electronics")
                .description("Electronic devices")
                .build();
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setSku("TEST-SKU");
        product.setStatus(ProductStatus.ACTIVE);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Test Product");
    }

    @Test
    public void shouldFindProductById() {
        Category category = Category.builder()
                .name("Books")
                .description("Books and literature")
                .build();
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Test Product 2");
        product.setPrice(BigDecimal.valueOf(200.0));
        product.setSku("TEST-SKU-2");
        product.setStatus(ProductStatus.ACTIVE);
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product 2");
    }
}
