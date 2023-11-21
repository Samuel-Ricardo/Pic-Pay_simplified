package com.picpay.payment.application.service.auth.token;

import com.picpay.payment.application.usecase.auth.jwt.GenerateJWTUseCase;
import com.picpay.payment.application.usecase.auth.jwt.GetTokenFromHeaderUseCase;
import com.picpay.payment.application.usecase.auth.jwt.ValidateJWTUseCase;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.policy.auth.NotRequireAuthInWhiteListPolicy;
import com.picpay.payment.domain.policy.auth.token.TokenMustExpireInTwoHoursPolicy;
import com.picpay.payment.domain.services.auth.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class JWTService implements TokenService {

    @Autowired
    private NotRequireAuthInWhiteListPolicy notRequireAuthInWhiteListPolicy;
    @Autowired
    private TokenMustExpireInTwoHoursPolicy tokenMustExpireInTwoHoursPolicy;

    @Autowired
    private GetTokenFromHeaderUseCase getTokenFromHeader;
    @Autowired
    private ValidateJWTUseCase validateJWT;
    @Autowired
    private GenerateJWTUseCase generateJTW;

    @Override
    public String generateFor(User user) {
        return generateJTW
                .execute(
                        user.getEmail(),
                        tokenMustExpireInTwoHoursPolicy.executeInBrazilOffset()
                );
    }

    @Override
    public Optional<String> validate(String token) {
        return validateJWT.execute(token);
    }

    @Override
    public Optional<String> getDataFrom(String token) {
        return validateJWT.execute(token);
    }

    @Override
    public Optional<String> getFromHeader(HttpServletRequest request) {
        return getTokenFromHeader.execute(request);
    }

    @Override
    public boolean shoudRequireToken(Runnable ignore, HttpServletRequest request) {
        return !notRequireAuthInWhiteListPolicy.execute(ignore, request);
    }
}
