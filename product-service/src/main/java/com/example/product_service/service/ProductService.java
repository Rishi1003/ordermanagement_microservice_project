package com.example.product_service.service;

import com.example.product_service.dto.requestDtos.ProductCreateUpdateRequest;
import com.example.product_service.dto.responseDtos.ProductResponse;
import com.example.product_service.entity.Category;
import com.example.product_service.entity.Product;
import com.example.product_service.exception.ResourceNotFoundException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateUpdateRequest request) {
        Product product = productMapper.toEntityProduct(request);

        if (product == null) {
            throw new IllegalArgumentException("Failed to map ProductCreateRequest to Product entity");
        }
        
        if (request.categoryId() == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        
        UUID categoryId = request.categoryId();
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (productRepository.existsBySku(request.sku())) {
            throw new IllegalArgumentException("Product with SKU '" + request.sku() + "' already exists");
        }

        product.setCategory(category);

        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Transactional
    public void softDeleteProduct(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductCreateUpdateRequest request) {

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + productId)
                );

        
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        if (!product.getSku().equals(request.sku())) {
            if (productRepository.existsBySku(request.sku())) {
                throw new IllegalArgumentException(
                        "Product with SKU '" + request.sku() + "' already exists"
                );
            }
            product.setSku(request.sku());
        }

        if (request.categoryId() != null) {

            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category not found with id: " + request.categoryId()
                            )
                    );

            product.setCategory(category);
        }

        product = productRepository.save(product);

        return productMapper.toProductResponse(product);
    }


    public ProductResponse getProductById(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return productMapper.toProductResponse(product);
    }

    public Page<ProductResponse> getProductsByCategory(UUID categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable);
        return products.map(productMapper::toProductResponse);
    }

    public Page<ProductResponse> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByDeletedFalse(pageable);
        return products.map(productMapper::toProductResponse);
    }
}
