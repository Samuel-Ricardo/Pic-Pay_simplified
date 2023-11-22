package com.picpay.payment.infra.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class CryptographyConfig {

    @Value("${app.security.token.secret}")
    private String AUHT_TOKEN_SECRET;

    @Bean
    @Primary
    @Lazy
    public Algorithm algorithm() {
        return Algorithm.HMAC256(AUHT_TOKEN_SECRET);
    }
}
