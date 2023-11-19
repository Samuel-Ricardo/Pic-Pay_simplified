package com.picpay.payment.application.usecase.user;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FindUserByIdUseCase {

    @Autowired
    private UserRepository repository;

    public User execute(UUID id) throws Exception {
        return this
                .repository
                .findUserById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }
}
