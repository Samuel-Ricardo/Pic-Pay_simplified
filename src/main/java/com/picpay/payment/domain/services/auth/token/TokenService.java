package com.picpay.payment.domain.services.auth.token;

import com.picpay.payment.domain.entities.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public interface TokenService {
    String generateFor(User user);
    Optional<String> validate(String token);
    Optional<String> getDataFrom(String token);
    Optional<String> getFromHeader(Function<String, String> header);
    boolean shoudRequireToken(Runnable ignore, String URLs);
}
