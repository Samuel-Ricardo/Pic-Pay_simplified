package com.picpay.payment.application.usecase.user;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveUserUseCase {

    @Autowired
    private UserRepository repository;

    public User execute(User user){
        return this.repository.save(user);
    }
}
