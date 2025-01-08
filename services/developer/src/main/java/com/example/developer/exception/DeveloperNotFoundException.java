package com.example.developer.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class DeveloperNotFoundException extends RuntimeException {
    private final String message;
}
