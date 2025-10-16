package com.example.auth.service;

import com.example.auth.dto.user.AdminUserResponse;
import com.example.auth.dto.user.CreateUserRequest;
import com.example.auth.dto.user.UpdateUserRequest;
import com.example.auth.entity.user.Role;
import com.example.auth.entity.user.User;
import com.example.auth.exception.AuthException;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.repository.TokenRepository;
import com.example.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

   @Transactional
    public void createUser(CreateUserRequest request) {
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .activated(false)
                .passwordExpired(false)
                .role(Role.USER)
                .build();

        var isEmailUsed = userRepository.findByEmail(request.email()).isPresent();
        if (isEmailUsed) {
            throw new AuthException("Email is already in use");
        }

        userRepository.save(user);
    }

    @Transactional
    public void createAdmin(CreateUserRequest request) {
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .activated(false)
                .passwordExpired(false)
                .role(Role.ADMIN)
                .build();

        var isEmailUsed = userRepository.findByEmail(request.email()).isPresent();
        if (isEmailUsed) {
            throw new AuthException("Email is already in use");
        }

        userRepository.save(user);
    }

    @Transactional
    public void update(UpdateUserRequest request) {
        var user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь с id %d не найден".formatted(request.userId())
                ));

        updateIfNotBlank(request.firstname(), user::setFirstname);
        updateIfNotBlank(request.lastname(), user::setLastname);
        updateIfNotBlank(request.password(), p -> user.setPassword(passwordEncoder.encode(p)));
        updateIfNotBlank(request.secretPhrase(), s -> user.setSecretPhrase(encryptionService.encrypt(s)));

        updateIfNotNull(request.activated(), user::setActivated);
        updateIfNotNull(request.passwordExpired(), user::setPasswordExpired);

        userRepository.save(user);
    }

    private void updateIfNotBlank(String value, Consumer<String> setter) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    @Transactional
    public void delete(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь с id %d не найден".formatted(userId)
                ));
        tokenRepository.deleteByUser(user);
        userRepository.delete(user);
    }

    public List<AdminUserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getEmail(),
                        user.getRole(),
                        user.isActivated(),
                        user.isPasswordExpired()
                ))
                .toList();
    }

    public AdminUserResponse findById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь с id %d не найден".formatted(userId)
                ));
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
