package com.picpay.payment.infra.config.security;

import com.picpay.payment.domain.entities.auth.Permissions;
import static com.picpay.payment.domain.entities.auth.Permissions.*;

import static com.picpay.payment.domain.entities.auth.Role.*;
import com.picpay.payment.infra.middleware.auth.jwt.JWTAuthMiddleware;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpMethod.*;
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

import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    public static final String[] WHITE_LIST = {
            "/",
            "/auth/login",
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
                        req
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(GET, "/users").hasAnyRole(USER.name(), ADMIN.name())
                                .requestMatchers(GET, "/transactions").hasAuthority(READ_TRANSACTION.getPermission())
                                .requestMatchers(POST, "/auth/register").hasAuthority(CREATE_USER.getPermission())
                                .requestMatchers(POST,"/transactions").hasAuthority(EXECUTE_TRANSACTION.getPermission())
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
