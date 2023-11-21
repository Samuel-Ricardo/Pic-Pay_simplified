package com.picpay.payment.application.usecase.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.picpay.payment.domain.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GenerateJWTUseCase {

    @Autowired
    private Algorithm ALGORITHM;

    public String execute(String email, Instant expiration) {
        return JWT.create()
                .withIssuer("auth-api")
                .withSubject(email)
                .withExpiresAt(expiration)
                .sign(ALGORITHM);
    }

}
