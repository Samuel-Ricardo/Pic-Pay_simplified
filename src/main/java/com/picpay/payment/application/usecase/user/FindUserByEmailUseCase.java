package com.picpay.payment.application.usecase.user;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindUserByEmailUseCase {

    @Autowired private UserRepository repository;

    Optional<User> execute(String email) {
        return repository.findByEmail(email);
    }
}
