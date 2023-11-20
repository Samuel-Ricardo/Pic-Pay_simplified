package com.picpay.payment.repositories.user;

import static com.picpay.payment.data.UserData.*;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("[UNIT] | Should: find by [document] => [USER]")
    void findUserByDocument() {

        this.entityManager.persist(VALID_COMON_USER);
        Optional<User> result = this.repository.findUserByDocument("12345678901");

        assertThat(result.isPresent()).isTrue();
    }



}
