package com.example.orders_service.mapper;

import com.example.orders_service.dto.OrderLineItemsDto;
import com.example.orders_service.dto.OrderRequest;
import com.example.orders_service.dto.OrderResponse;
import com.example.orders_service.entity.Order;
import com.example.orders_service.entity.OrderLineItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orderLineItems", source = "orderLineItems")
    Order toEntityOrder(OrderRequest request);

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderLineItems toEntityOrderLineItems(OrderLineItemsDto dto);

    OrderLineItemsDto toOrderLineItemsDto(OrderLineItems entity);
}
