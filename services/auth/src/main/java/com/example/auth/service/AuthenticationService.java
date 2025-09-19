package com.example.auth.service;

import com.example.auth.dto.AuthenticationRequest;
import com.example.auth.dto.AuthenticationResponse;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.entity.token.Token;
import com.example.auth.entity.user.Role;
import com.example.auth.entity.user.User;
import com.example.auth.exception.AuthException;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.repository.TokenRepository;
import com.example.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        var isEmailUsed = userRepository.findByEmail(request.email()).isPresent();
        if (isEmailUsed) {
            throw new AuthException("Email is already in use");
        }

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        if (isMissingBearerToken(request)) {
            return;
        }
        var refreshToken = getJwtToken(request);

        var userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            throw new AuthException("Email is invalid");
        }

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (isAccessToken(refreshToken)) {
            throw new AuthException("This is an access token, needed a refresh token");
        }

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new AuthException("Invalid refresh token");
        }

        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = new AuthenticationResponse(accessToken, refreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        if (isMissingBearerToken(request)) {
            return;
        }
        var accessToken = getJwtToken(request);
        var token = tokenRepository.findByToken(
                encryptionService.encrypt(accessToken)
        );
        if (token.isEmpty() || token.get().isRevoked()) {
            throw new AuthException("Invalid access token");
        }
        revokeAllUserTokens(token.get().user);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(encryptionService.encrypt(jwtToken))
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findByRevokedFalseAndUser_Id(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    private boolean isAccessToken(String refreshToken) {
        return tokenRepository.findByToken(
                encryptionService.encrypt(refreshToken)
        ).isPresent();
    }

    private boolean isMissingBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader == null ||!authHeader.startsWith("Bearer ");
    }

    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public Boolean isTokenValid(String token) {
        var userEmail = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        var isNotRevoked = tokenRepository.findByToken(encryptionService.encrypt(token))
                .map(t -> !t.isRevoked())
                .orElse(false);

        return isNotRevoked && jwtService.isTokenValid(token, userDetails);
    }
}
