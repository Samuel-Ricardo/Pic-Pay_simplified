package com.picpay.payment.service.transaction;

import com.picpay.payment.application.service.TransactionServiceImpl;
import com.picpay.payment.application.usecase.transaction.SaveTransactionUseCase;
import com.picpay.payment.application.usecase.transaction.TransactionUseCase;
import static com.picpay.payment.data.UserData.*;

import static com.picpay.payment.data.TransactionData.*;
import com.picpay.payment.domain.policy.transaction.CannotTransactWithoutSufficientBalancePolicy;
import com.picpay.payment.domain.policy.transaction.MerchantUserCantTransactPolicy;
import com.picpay.payment.domain.policy.transaction.SendNotificationOnTransactPolicy;
import com.picpay.payment.domain.services.AuthorizationService;
import com.picpay.payment.domain.services.UserService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.openMocks;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@ActiveProfiles("test")
@SpringBootTest
public class TransactionServiceTest {

    @Mock private AuthorizationService authorizationService;
    @Mock private UserService userService;

    @Mock private MerchantUserCantTransactPolicy merchantUserCantTransactPolicy;
    @Mock private CannotTransactWithoutSufficientBalancePolicy cannotTransactWithoutSufficientBalancePolicy;
    @Mock private SendNotificationOnTransactPolicy sendNotificationOnTransactPolicy;

    @Mock private TransactionUseCase transct;
    @Mock private SaveTransactionUseCase save;

    @Autowired
    @InjectMocks
    private TransactionServiceImpl service;

    @BeforeEach
    void setup(){
        openMocks(this);
    }

    @Test
    @DisplayName("[UNIT] | Should: validate => [TRANSACTIONS]")
    void validate() throws Exception {

        var value = new BigDecimal(100);

        when(cannotTransactWithoutSufficientBalancePolicy.execute(VALID_TRANSACTION.getSender(), value))
                .thenReturn(false);

        when(merchantUserCantTransactPolicy.execute(VALID_TRANSACTION.getSender().getUserType()))
                .thenReturn(false);

        when(authorizationService.isTransactionAuthorized(VALID_TRANSACTION.getSender(), value))
                .thenReturn(true);

        assertTrue(service.validate(VALID_TRANSACTION.getSender(), value));

        verify(cannotTransactWithoutSufficientBalancePolicy, times(1))
                .execute(VALID_TRANSACTION.getSender(), value);

        verify(merchantUserCantTransactPolicy, times(1))
                .execute(VALID_TRANSACTION.getSender().getUserType());

        verify(authorizationService, times(1))
                .isTransactionAuthorized(VALID_TRANSACTION.getSender(), value);
    }
}
