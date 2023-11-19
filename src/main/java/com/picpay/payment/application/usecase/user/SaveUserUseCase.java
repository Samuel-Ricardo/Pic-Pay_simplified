package com.picpay.payment.application.usecase.user;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveUserUseCase {

    @Autowired
    private UserRepository repository;

    public User execute(User user){
        return this.repository.save(user);
    }
}
