package com.picpay.payment.infra.middleware.auth.jwt;

import com.picpay.payment.domain.repositories.UserRepository;
import com.picpay.payment.domain.services.UserService;
import com.picpay.payment.domain.services.auth.AuthContextService;
import com.picpay.payment.domain.services.auth.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthMiddleware extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;
    private final AuthContextService auth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        if(!tokenService.shoudRequireToken(() -> next(filterChain, request, response), request.getServletPath())) return;

        tokenService.getFromHeader(request::getHeader)
                .flatMap(tokenService::validate)
                .flatMap(userService::findUserByEmail)
                .ifPresent(auth::saveContext);
        ;

        next(filterChain, request, response);
    }

    private void next(FilterChain filterChain, HttpServletRequest request, HttpServletResponse response ) {
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
