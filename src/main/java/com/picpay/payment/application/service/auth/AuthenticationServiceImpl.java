package com.picpay.payment.application.service.auth;

import com.picpay.payment.domain.dto.auth.LoginDTO;
import com.picpay.payment.domain.dto.auth.LoginResponseDTO;
import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public LoginResponseDTO login(LoginDTO DTO) {
        return null;
    }

    @Override
    public User register(UserDTO DTO) {
        return null;
    }
}
