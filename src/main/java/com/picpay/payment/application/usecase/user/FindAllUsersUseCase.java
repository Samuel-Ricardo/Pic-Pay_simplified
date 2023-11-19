package com.picpay.payment.application.usecase.user;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindAllUsersUseCase {

    @Autowired
    private UserRepository repository;

    public List<User> execute() {
        return this.repository.findAll();
    }
}
