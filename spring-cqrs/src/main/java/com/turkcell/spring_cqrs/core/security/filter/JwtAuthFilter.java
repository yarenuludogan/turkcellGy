package com.turkcell.spring_cqrs.core.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.turkcell.spring_cqrs.core.security.context.UserContext;
import com.turkcell.spring_cqrs.core.security.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserContext userContext;

    public JwtAuthFilter(JwtService jwtService, UserContext userContext) {
        this.jwtService = jwtService;
        this.userContext = userContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtHeader = request.getHeader("Authorization");

        if (jwtHeader != null && jwtHeader.startsWith(BEARER_PREFIX) && jwtHeader.length() > BEARER_PREFIX.length()) {
            String token = jwtHeader.substring(BEARER_PREFIX.length());

            try {
                if (jwtService.isTokenValid(token)) {
                    String userId = jwtService.extractUserId(token);
                    String email = jwtService.extractEmail(token);
                    List<String> roles = jwtService.extractRoles(token);
                    userContext.setUser(userId, email, roles != null ? roles : List.of());
                }
            } catch (Exception ignored) {
                // Geçersiz imza / parse: kullanıcı anonim kalır; korumalı istekler mediator'da reddedilir.
            }
        }

        filterChain.doFilter(request, response);
    }
}
