package com.picpay.payment.application.service.auth;

import com.picpay.payment.application.usecase.auth.AuthenticateUserUseCase;
import com.picpay.payment.domain.dto.auth.LoginDTO;
import com.picpay.payment.domain.dto.auth.LoginResponseDTO;
import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.policy.security.EncryptPasswordBeforeRegisteringUserPolicy;
import com.picpay.payment.domain.services.UserService;
import com.picpay.payment.domain.services.auth.AuthenticationService;
import com.picpay.payment.domain.services.auth.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenService tokenService;
    private final UserService userService;

    private final AuthenticateUserUseCase authenticate;

    private final EncryptPasswordBeforeRegisteringUserPolicy encryptPasswordBeforeRegisteringUserPolicy;

    @Override
    public LoginResponseDTO login(LoginDTO DTO) {
        var auth = authenticate.execute(DTO);
        var token = tokenService.generateFor((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }

    @Override
    public User register(UserDTO DTO) throws Exception {

        if(userService.findUserByEmail(DTO.email()).isPresent()) throw new Exception("User Already Exists");

        var user = User.from(DTO);
        encryptPasswordBeforeRegisteringUserPolicy.execute(DTO.password(), user::setPassword);
        return userService.saveUser(user);
    }
}
