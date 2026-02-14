package com.example.product_service.mapper;

import com.example.product_service.dto.requestDtos.CategoryCreateRequest;
import com.example.product_service.dto.responseDtos.CategoryResponse;
import com.example.product_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    Category toEntity(CategoryCreateRequest request);
    
    @Mapping(source = "id", target = "categoryId")
    CategoryResponse toResponse(Category category);
}