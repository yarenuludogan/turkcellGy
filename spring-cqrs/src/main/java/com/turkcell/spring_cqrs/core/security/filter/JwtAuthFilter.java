package com.turkcell.spring_cqrs.core.security.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.turkcell.spring_cqrs.core.security.context.UserContext;
import com.turkcell.spring_cqrs.core.security.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Her istekte devreye gir, varsa JWT'i doğrula ve sisteme bak bu kişi şu jwt ile girdi bilgisini tanıt.. 

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserContext userContext;
    
    public JwtAuthFilter(JwtService jwtService, UserContext userContext) {
        this.jwtService = jwtService;
        this.userContext = userContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                // request -> istek
                // response -> response'ın o ana kadarki oluşan halini 
                // filterChain -> zincirin kendisi
            String jwtHeader = request.getHeader("Authorization");

            if(jwtHeader != null) {
                String token = jwtHeader.substring(7);

                // JWT'i doğrula, kullanıcıyı bul ve sisteme tanıt..
                try{
                    if(jwtService.isTokenValid(token))
                    {
                        // Kullanıcıyı sisteme tanıt..
                        String userId = jwtService.extractUserId(token);
                        String email = jwtService.extractEmail(token);
                        List<String> roles = jwtService.extractRoles(token);
                        userContext.setUser(userId, email, roles);
                    }
                }catch(Exception e){
                    // SecurityContextHolder.Clear();
                }
            }

            filterChain.doFilter(request, response); // chaini ilerlet..
    }

}
