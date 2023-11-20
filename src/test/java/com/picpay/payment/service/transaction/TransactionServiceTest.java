package com.picpay.payment.service.transaction;

import com.picpay.payment.application.service.TransactionServiceImpl;
import com.picpay.payment.application.usecase.transaction.SaveTransactionUseCase;
import com.picpay.payment.application.usecase.transaction.TransactionUseCase;
import com.picpay.payment.domain.policy.transaction.CannotTransactWithoutSufficientBalancePolicy;
import com.picpay.payment.domain.policy.transaction.MerchantUserCantTransactPolicy;
import com.picpay.payment.domain.policy.transaction.SendNotificationOnTransactPolicy;
import com.picpay.payment.domain.services.AuthorizationService;
import com.picpay.payment.domain.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

    

}
