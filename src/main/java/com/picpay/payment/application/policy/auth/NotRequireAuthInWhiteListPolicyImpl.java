package com.picpay.payment.application.policy.auth;

import com.picpay.payment.domain.policy.auth.NotRequireAuthInWhiteListPolicy;
import static com.picpay.payment.infra.config.SecurityConfig.WHITE_LIST;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class NotRequireAuthInWhiteListPolicyImpl implements NotRequireAuthInWhiteListPolicy {
    @Override
    public boolean execute(
            FilterChain filterChain,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        var empty = Arrays
                .stream(WHITE_LIST)
                .filter( url ->
                        request
                                .getServletPath()
                                .contains(url)
                )
                .toList()
                .isEmpty();

        if (empty) filterChain.doFilter(request, response);
        return empty;
    }
}
