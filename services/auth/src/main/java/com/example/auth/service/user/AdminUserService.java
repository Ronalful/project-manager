package com.example.auth.service.user;

import com.example.auth.dto.user.CreateUserRequest;
import com.example.auth.entity.user.Role;
import com.example.auth.entity.user.User;
import com.example.auth.exception.AuthException;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(CreateUserRequest request) {
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .activated(false)
                .role(Role.USER)
                .build();

        var isEmailUsed = userRepository.findByEmail(request.email()).isPresent();
        if (isEmailUsed) {
            throw new AuthException("Email is already in use");
        }

        userRepository.save(user);
    }

    public void createAdmin(CreateUserRequest request) {
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .activated(false)
                .role(Role.ADMIN)
                .build();

        var isEmailUsed = userRepository.findByEmail(request.email()).isPresent();
        if (isEmailUsed) {
            throw new AuthException("Email is already in use");
        }

        userRepository.save(user);
    }
}
