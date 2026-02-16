package com.example.user_service.mapper;

import com.example.user_service.dto.requestDtos.UserCreateUpdateRequest;
import com.example.user_service.dto.responseDtos.UserResponse;
import com.example.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    // Password will be handled manually in the service to ensure hashing
    @Mapping(target = "password", ignore = true) 
    User toEntityUser(UserCreateUpdateRequest request);
}
