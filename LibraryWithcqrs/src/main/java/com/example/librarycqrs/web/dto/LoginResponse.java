package com.example.librarycqrs.web.dto;

public record LoginResponse(
        String token,
        long expiresInSeconds
) {
}
