package com.example.auth.repository;

import com.example.auth.entity.token.Token;
import com.example.auth.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByRevokedFalseAndUser_Id(Long id);
    Optional<Token> findByToken(String token);
    void deleteByUser(User user);
}
