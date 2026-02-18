package com.example.user_service.service;

import com.example.user_service.dto.requestDtos.UserCreateUpdateRequest;
import com.example.user_service.dto.requestDtos.UserLoginRequest;
import com.example.user_service.dto.responseDtos.UserResponse;
import com.example.user_service.dto.responseDtos.UserVerificationResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.ResourceNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(UserCreateUpdateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username '" + request.username() + "' already exists");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email '" + request.email() + "' already exists");
        }

        // Public registration only allows CUSTOMER role
        if (request.role() != com.example.user_service.entity.Role.CUSTOMER) {
            throw new IllegalArgumentException("Only CUSTOMER role can be created via public registration");
        }

        User user = userMapper.toEntityUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    // should be role based idealy only admin should be able to do certain things
    @Transactional
    public UserResponse updateUser(UUID id, UserCreateUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username '" + request.username() + "' already exists");
        }
        if (!user.getEmail().equals(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email '" + request.email() + "' already exists");
        }

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setRole(request.role());

        user.setPassword(passwordEncoder.encode(request.password()));

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toUserResponse(user);
    }

    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return userMapper.toUserResponse(user);
    }

    public UserVerificationResponse verifyUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with identifier: " + request.email()));

        boolean isValid = passwordEncoder.matches(request.password(), user.getPassword());

        return new UserVerificationResponse(isValid,
                isValid ? "Verification successful" : "Invalid credentials", null, user.getRole().name());
    }
}
