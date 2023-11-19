package com.picpay.payment.domain.services;

import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User findUserById(Long id) throws Exception;

    public User createUser(UserDTO data);

    public List<User> getAllUsers();

    public void saveUser(User user);

}
