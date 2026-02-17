package com.example.inventory_service.mapper;

import com.example.inventory_service.dto.requestDtos.InventoryCreateUpdateRequest;
import com.example.inventory_service.dto.responseDtos.InventoryResponse;
import com.example.inventory_service.entity.Inventory;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-17T07:54:20+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class InventoryMapperImpl implements InventoryMapper {

    @Override
    public InventoryResponse toInventoryResponse(Inventory entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        UUID productId = null;
        Integer availableQuantity = null;
        Integer reservedQuantity = null;
        Instant updatedAt = null;

        id = entity.getId();
        productId = entity.getProductId();
        availableQuantity = entity.getAvailableQuantity();
        reservedQuantity = entity.getReservedQuantity();
        updatedAt = entity.getUpdatedAt();

        InventoryResponse inventoryResponse = new InventoryResponse( id, productId, availableQuantity, reservedQuantity, updatedAt );

        return inventoryResponse;
    }

    @Override
    public Inventory toEntityInventory(InventoryCreateUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        Inventory.InventoryBuilder inventory = Inventory.builder();

        inventory.availableQuantity( request.availableQuantity() );
        inventory.productId( request.productId() );
        inventory.reservedQuantity( request.reservedQuantity() );

        return inventory.build();
    }
}
