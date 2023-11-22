package com.picpay.payment.application.service.auth;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthContextService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthContextServiceImpl implements AuthContextService {

    @Override
    public void saveContext(User user) {
        
    }

    @Override
    public Optional<User> getUser() {
        return Optional.empty();
    }
}
