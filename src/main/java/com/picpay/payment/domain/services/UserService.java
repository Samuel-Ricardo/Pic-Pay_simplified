package com.picpay.payment.domain.services;

import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    public User findUserById(UUID id) throws Exception;

    public User saveUser(UserDTO data);

    public User saveUser(User data);

    public List<User> findAllUsers();

}
