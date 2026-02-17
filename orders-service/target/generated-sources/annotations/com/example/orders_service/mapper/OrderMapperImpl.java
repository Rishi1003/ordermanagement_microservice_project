package com.example.orders_service.mapper;

import com.example.orders_service.dto.OrderLineItemsDto;
import com.example.orders_service.dto.OrderRequest;
import com.example.orders_service.dto.OrderResponse;
import com.example.orders_service.entity.Order;
import com.example.orders_service.entity.OrderLineItems;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-17T07:55:28+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Microsoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntityOrder(OrderRequest request) {
        if ( request == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.orderLineItems( orderLineItemsDtoListToOrderLineItemsList( request.orderLineItems() ) );
        order.userId( request.userId() );

        return order.build();
    }

    @Override
    public OrderResponse toOrderResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID id = null;
        String orderNumber = null;
        UUID userId = null;
        List<OrderLineItemsDto> orderLineItems = null;
        Instant createdAt = null;

        id = order.getId();
        orderNumber = order.getOrderNumber();
        userId = order.getUserId();
        orderLineItems = orderLineItemsListToOrderLineItemsDtoList( order.getOrderLineItems() );
        createdAt = order.getCreatedAt();

        OrderResponse orderResponse = new OrderResponse( id, orderNumber, userId, orderLineItems, createdAt );

        return orderResponse;
    }

    @Override
    public OrderLineItems toEntityOrderLineItems(OrderLineItemsDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderLineItems.OrderLineItemsBuilder orderLineItems = OrderLineItems.builder();

        orderLineItems.skuCode( dto.skuCode() );
        orderLineItems.price( dto.price() );
        orderLineItems.quantity( dto.quantity() );

        return orderLineItems.build();
    }

    @Override
    public OrderLineItemsDto toOrderLineItemsDto(OrderLineItems entity) {
        if ( entity == null ) {
            return null;
        }

        String skuCode = null;
        BigDecimal price = null;
        Integer quantity = null;

        skuCode = entity.getSkuCode();
        price = entity.getPrice();
        quantity = entity.getQuantity();

        OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto( skuCode, price, quantity );

        return orderLineItemsDto;
    }

    protected List<OrderLineItems> orderLineItemsDtoListToOrderLineItemsList(List<OrderLineItemsDto> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderLineItems> list1 = new ArrayList<OrderLineItems>( list.size() );
        for ( OrderLineItemsDto orderLineItemsDto : list ) {
            list1.add( toEntityOrderLineItems( orderLineItemsDto ) );
        }

        return list1;
    }

    protected List<OrderLineItemsDto> orderLineItemsListToOrderLineItemsDtoList(List<OrderLineItems> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderLineItemsDto> list1 = new ArrayList<OrderLineItemsDto>( list.size() );
        for ( OrderLineItems orderLineItems : list ) {
            list1.add( toOrderLineItemsDto( orderLineItems ) );
        }

        return list1;
    }
}
