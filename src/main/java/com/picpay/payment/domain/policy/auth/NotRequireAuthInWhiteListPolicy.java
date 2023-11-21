package com.picpay.payment.domain.policy.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import java.io.IOException;
import java.util.Optional;

public interface NotRequireAuthInWhiteListPolicy {
    boolean execute(
            FilterChain filterChain,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException;
}
