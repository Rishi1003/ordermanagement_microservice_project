package com.example.product_service.repository;

import com.example.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByCategoryIdAndDeletedFalse(UUID categoryId, Pageable pageable);

    Page<Product> findByDeletedFalse(Pageable pageable);

    boolean existsBySku(String sku);
}
