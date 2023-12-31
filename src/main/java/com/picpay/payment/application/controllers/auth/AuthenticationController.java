package com.picpay.payment.application.controllers.auth;

import com.picpay.payment.domain.dto.auth.LoginDTO;
import com.picpay.payment.domain.dto.auth.LoginResponseDTO;
import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody
            @Valid LoginDTO DTO
        ) throws Exception {
        return ResponseEntity.ok(service.login(DTO));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody
            @Valid UserDTO DTO
            ) throws Exception {

        return ResponseEntity.ok(service.register(DTO));
    }

}
