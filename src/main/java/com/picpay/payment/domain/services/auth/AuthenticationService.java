package com.picpay.payment.domain.services.auth;

import com.picpay.payment.domain.dto.auth.LoginDTO;
import com.picpay.payment.domain.dto.auth.LoginResponseDTO;
import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;

public interface AuthenticationService {
    LoginResponseDTO login(LoginDTO DTO);
    User register(UserDTO DTO) throws Exception;
}
