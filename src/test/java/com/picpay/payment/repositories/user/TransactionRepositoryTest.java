package com.picpay.payment.repositories.user;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.repositories.TransactionRepository;
import com.picpay.payment.domain.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.picpay.payment.data.UserData.VALID_COMON_USER;
import static com.picpay.payment.data.UserData.VALID_MERCHANT_USER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {

    @Autowired
    TransactionRepository repository;

    @Test
    @DisplayName("[UNIT] | Should: find [all] => [TRANSACTION]")
    void findUserByDocument() {
        assertThat(repository.findAll()).isNotNull();
    }

}
