package com.picpay.payment.application.service;

import com.picpay.payment.application.usecase.user.FindAllUsersUseCase;
import com.picpay.payment.application.usecase.user.FindUserByIdUseCase;
import com.picpay.payment.application.usecase.user.SaveUserUseCase;
import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private FindUserByIdUseCase findById;
    @Autowired
    private FindAllUsersUseCase findAll;
    @Autowired
    private SaveUserUseCase save;

    @Override
    public User findUserById(UUID id) throws Exception {
        return findById.execute(id);
    }

    @Override
    public User saveUser(UserDTO data) {
        return save.execute(User.from(data));
    }

    @Override
    public List<User> findAllUsers() {
        return findAll.execute();
    }
}
