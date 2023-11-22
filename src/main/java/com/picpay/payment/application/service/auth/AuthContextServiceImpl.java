package com.picpay.payment.application.service.auth;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthContextService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthContextServiceImpl implements AuthContextService {

    @Override
    public void saveContext(User user) {
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Optional<User> getUser() {
        return Optional.empty();
    }
}
