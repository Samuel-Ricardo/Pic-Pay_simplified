package com.picpay.payment.application.usecase.auth;

import com.picpay.payment.domain.dto.auth.LoginDTO;
import com.picpay.payment.domain.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserUseCase {

    @Autowired
    private AuthenticationManager authenticationManager;

    public Authentication execute(LoginDTO DTO){
        var credentials = new UsernamePasswordAuthenticationToken(DTO.email(), DTO.password());
        return authenticationManager.authenticate(credentials);
    }
}
