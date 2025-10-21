package com.example.auth.mapper;

import com.example.auth.dto.user.AdminUserResponse;
import com.example.auth.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    public AdminUserResponse fromUser(User user) {
        return new AdminUserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                user.isActivated(),
                user.isPasswordExpired()
        );
    }
}
