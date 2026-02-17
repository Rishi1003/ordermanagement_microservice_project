package com.example.user_service.mapper;

import com.example.user_service.dto.requestDtos.UserCreateUpdateRequest;
import com.example.user_service.dto.responseDtos.UserResponse;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-17T07:54:21+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toUserResponse(User entity) {
        if ( entity == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        String email = null;
        Role role = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = entity.getId();
        username = entity.getUsername();
        email = entity.getEmail();
        role = entity.getRole();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        UserResponse userResponse = new UserResponse( id, username, email, role, createdAt, updatedAt );

        return userResponse;
    }

    @Override
    public User toEntityUser(UserCreateUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.email() );
        user.role( request.role() );
        user.username( request.username() );

        return user.build();
    }
}
