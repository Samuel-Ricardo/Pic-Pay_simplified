package com.picpay.payment.application.usecase.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateJWTUseCase {

    @Autowired
    private Algorithm ALGORITHM;

    public String execute(String token) {
        return JWT.require(ALGORITHM)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
    }
}
