package com.picpay.payment.service.transaction;

import com.picpay.payment.application.service.TransactionServiceImpl;
import com.picpay.payment.application.usecase.transaction.SaveTransactionUseCase;
import com.picpay.payment.application.usecase.transaction.TransactionUseCase;
import static com.picpay.payment.data.UserData.*;

import static com.picpay.payment.data.TransactionData.*;

import com.picpay.payment.data.TransactionData;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.entities.user.User;
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
import java.util.UUID;

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

    @Test
    @DisplayName("[UNIT] | Should: execute => [TRANSACTION]")
    void transact() throws Exception {

        var value = new BigDecimal(100);

        when(cannotTransactWithoutSufficientBalancePolicy.execute(any(User.class), eq(value)))
                .thenReturn(false);
        when(merchantUserCantTransactPolicy.execute(SENDER.getUserType()))
                .thenReturn(false);
        when(authorizationService.isTransactionAuthorized(any(SENDER.getClass()), eq(value)))
                .thenReturn(true);


        when(userService.findUserById(SENDER.getId()))
                .thenReturn(SENDER);
        when(userService.findUserById(RECEIVER.getId()))
                .thenReturn(RECEIVER);


        when(transct.execute(any(Transaction.class))).thenReturn(VALID_TRANSACTION);
        when(save.execute(any(Transaction.class))).thenReturn(VALID_TRANSACTION);

        when(userService.saveUser(any(User.class))).thenReturn(any(User.class));


        var result = service.transact(VALID_TRANSACTION_DATA);
        assertEquals(VALID_TRANSACTION.getId(), result.getId());


        verify(cannotTransactWithoutSufficientBalancePolicy, times(1))
                .execute(any(User.class), eq(value));
        verify(merchantUserCantTransactPolicy, times(1))
                .execute(SENDER.getUserType());
        verify(authorizationService, times(1))
                .isTransactionAuthorized(any(User.class), eq(value));


        verify(userService, times(2)).findUserById(any(UUID.class));

        verify(transct,times(1))
                .execute(any(Transaction.class));
        verify(save, times(1))
                .execute(any(Transaction.class));

        verify(userService,times(2)).saveUser(any(User.class));

        verify(sendNotificationOnTransactPolicy, times(1))
                .execute(any(Transaction.class));
    }
}
