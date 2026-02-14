package com.example.product_service.mapper;

import org.mapstruct.Mapper;
// import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import com.example.product_service.dto.requestDtos.ProductCreateUpdateRequest;
import com.example.product_service.dto.responseDtos.ProductResponse;
import com.example.product_service.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toProductResponse(Product entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "status", ignore = true)
    Product toEntityProduct(ProductCreateUpdateRequest request);
    // void updateEntity(UpdateProductDTO dto, 
    //               @MappingTarget Product entity);

}
