package com.picpay.payment.application.usecase.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Lazy
public class ValidateJWTUseCase {

    @Autowired
    private Algorithm ALGORITHM;

    public Optional<String> execute(String token) {
        try {
            return Optional.of(
                    JWT.require(ALGORITHM)
                            .withIssuer("auth-api")
                            .build()
                            .verify(token)
                            .getSubject()
            );
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
