package com.picpay.payment.domain.policy.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;

public interface NotRequireAuthInWhiteListPolicy {
    void execute(FilterChain filterChain, HttpServletRequest request);
}
