package com.picpay.payment.domain.services.auth;

import com.picpay.payment.domain.entities.user.User;

import java.util.Optional;

public interface AuthContextService {
    void saveContext(User user);
    Optional<User> getUser();

    void clearContext();
}
