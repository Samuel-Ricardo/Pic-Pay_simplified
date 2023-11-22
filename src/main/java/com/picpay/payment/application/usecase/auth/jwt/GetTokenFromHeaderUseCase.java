package com.picpay.payment.application.usecase.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class GetTokenFromHeaderUseCase {
    public Optional<String> execute(Function<String, String> header){

        var token = header.apply("Authorization");
        return token == null? Optional.empty()
                : Optional.of(token.replace("Bearer ", ""));
    }
}
