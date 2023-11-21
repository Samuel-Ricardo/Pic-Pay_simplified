package com.picpay.payment.domain.services.auth.token;

import com.picpay.payment.domain.entities.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface TokenService {
    String generateFor(User user);
    Optional<String> validate(String token);
    Optional<String> getDataFrom(String token);
    Optional<String> getFromHeader(HttpServletRequest request);
    boolean shoudRequireToken(FilterChain filterChain, HttpServletRequest request, HttpServletResponse response);
}
