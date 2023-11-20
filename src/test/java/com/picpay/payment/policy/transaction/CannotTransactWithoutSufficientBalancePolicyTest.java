package com.picpay.payment.policy.transaction;

import static com.picpay.payment.data.UserData.*;
import com.picpay.payment.domain.policy.transaction.CannotTransactWithoutSufficientBalancePolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

@ActiveProfiles("test")
@SpringBootTest
public class CannotTransactWithoutSufficientBalancePolicyTest {

    @Autowired
    private CannotTransactWithoutSufficientBalancePolicy policy;



}
