package com.example.inventory_service.mapper;

import com.example.inventory_service.dto.requestDtos.InventoryCreateUpdateRequest;
import com.example.inventory_service.dto.responseDtos.InventoryResponse;
import com.example.inventory_service.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryResponse toInventoryResponse(Inventory entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Inventory toEntityInventory(InventoryCreateUpdateRequest request);
}
