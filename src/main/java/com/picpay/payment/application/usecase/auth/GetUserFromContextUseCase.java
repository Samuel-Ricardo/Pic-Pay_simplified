package com.picpay.payment.application.usecase.auth;

import com.picpay.payment.domain.entities.user.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserFromContextUseCase {
    public Optional<User> execute(){
        var authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken
        ) { return Optional.empty(); }

        return Optional.ofNullable((User) authentication.getPrincipal());
    }
}
