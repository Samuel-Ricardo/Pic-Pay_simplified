package com.picpay.payment.infra.config.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.picpay.payment.infra.middleware.auth.jwt.JWTAuthMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    public static final String[] WHITE_LIST = {
            "/",
            "/auth/**",
            "/v3/api-docs/**",
            "/api-docs.yaml",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html"
    };

    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final JWTAuthMiddleware JWT_MIDDLEWARE;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                ).
                sessionManagement(session -> session.sessionCreationPolicy(STATELESS) ).
                authenticationProvider(authenticationProvider).
                addFilterBefore(JWT_MIDDLEWARE, UsernamePasswordAuthenticationFilter.class).
                logout(logout -> logout.
                        logoutUrl("/auth/logout").
                        addLogoutHandler(logoutHandler).
                        logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                )
        ;
        return http.build();
    }
}