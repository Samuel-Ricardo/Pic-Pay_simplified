package com.picpay.payment.application.usecase.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetTokenFromHeaderUseCase {
    public Optional<String> execute(HttpServletRequest request){
        var token = request.getHeader("Authorization");

        return token == null? Optional.empty()
                : Optional.of(token.replace("Bearer ", ""));
    }
}
