package com.picpay.payment.domain.services.auth.token;

import com.picpay.payment.domain.entities.user.User;

import java.util.Optional;

public interface TokenService {
    String generateFor(User user);
    String validate(String token);
    Optional<String> getDataFrom(String token);
}
