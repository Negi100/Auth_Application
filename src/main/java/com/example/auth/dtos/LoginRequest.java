package com.example.auth.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
