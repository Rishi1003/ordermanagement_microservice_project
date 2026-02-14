package com.example.product_service.service;

import com.example.product_service.dto.requestDtos.CategoryCreateRequest;
import com.example.product_service.dto.requestDtos.CategoryUpdateRequest;
import com.example.product_service.dto.responseDtos.CategoryResponse;
import com.example.product_service.entity.Category;
import com.example.product_service.exception.ResourceNotFoundException;
import com.example.product_service.repository.CategoryRepository;
import com.example.product_service.mapper.CategoryMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;

	public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
		this.categoryRepository = categoryRepository;
		this.categoryMapper = categoryMapper;
	}

	@Transactional
	public CategoryResponse createCategory(CategoryCreateRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}

		String name = request.name();
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("category name must not be blank");
		}

		categoryRepository.findByName(name.trim()).ifPresent(c -> {
			throw new DataIntegrityViolationException("Category with name '" + name + "' already exists");
		});

		Category toSave = categoryMapper.toEntity(request);
		
		toSave.setName(Objects.requireNonNull(name).trim());

		Category saved = categoryRepository.save(toSave);

		return categoryMapper.toResponse(saved);
	}

	@Transactional
	public CategoryResponse updateCategory(CategoryUpdateRequest request, UUID id) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}

		UUID categoryId = id;
		if (categoryId == null) {
			throw new IllegalArgumentException("categoryId must not be null");
		}

		String name = request.name();
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("category name must not be blank");
		}

		Category existing = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + categoryId + "' not found"));

		String trimmed = Objects.requireNonNull(name).trim();

		categoryRepository.findByName(trimmed).ifPresent(c -> {
			if (!c.getId().equals(categoryId)) {
				throw new DataIntegrityViolationException("Category with name '" + name + "' already exists");
			}
		});

		existing.setName(trimmed);
		existing.setDescription(request.description());

		Category saved = categoryRepository.save(existing);

		return categoryMapper.toResponse(saved);
	}

	@Transactional
	public CategoryResponse deactivateCategory(UUID categoryId) {
		if (categoryId == null) {
			throw new IllegalArgumentException("categoryId must not be null");
		}

		Category existing = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + categoryId + "' not found"));

		if (!existing.isActive()) {
			throw new IllegalArgumentException("Category with id '" + categoryId + "' is already inactive");
		}

		existing.setActive(false);

		Category saved = categoryRepository.save(existing);

		return categoryMapper.toResponse(saved);
	}

	@Transactional
	public CategoryResponse activateCategory(UUID categoryId) {
		if (categoryId == null) {
			throw new IllegalArgumentException("categoryId must not be null");
		}

		Category existing = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category with id '" + categoryId + "' not found"));

		if (existing.isActive()) {
			throw new IllegalArgumentException("Category with id '" + categoryId + "' is already active");
		}

		existing.setActive(true);

		Category saved = categoryRepository.save(existing);

		return categoryMapper.toResponse(saved);
	}

	public List<CategoryResponse> getAllCategories() {
		return categoryRepository.findAll()
				.stream()
				.map(categoryMapper::toResponse)
				.toList();
	}

	public List<CategoryResponse> getActiveCategories() {
		return categoryRepository.findAllByActiveTrue()
				.stream()
				.map(categoryMapper::toResponse)
				.toList();
	}
}
