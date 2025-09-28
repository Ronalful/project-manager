package com.example.auth.dto;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}
