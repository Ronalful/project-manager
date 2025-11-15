package com.example.auth.dto.user;

import com.example.auth.entity.user.Role;

public record UserResponse(
        Long id,
        String firstname,
        String lastname,
        String email,
        Role role
){
  }