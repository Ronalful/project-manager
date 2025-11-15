package com.example.auth.service;

import com.example.auth.dto.user.UserResponse;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.mapper.UserMapper;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponse getInfo(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapper.toUserResponse(user);
    }
}
