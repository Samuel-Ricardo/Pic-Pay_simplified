package com.picpay.payment.domain.repositories;

import com.picpay.payment.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByDocument(String document);
    Optional<User> findUserById(Long id);
}
