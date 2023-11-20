package com.picpay.payment.service.authorization;

import com.picpay.payment.application.service.AuthorizationServiceImpl;
import com.picpay.payment.application.usecase.authorization.IsAuthorizedUseCase;
import static com.picpay.payment.data.UserData.*;
import com.picpay.payment.domain.services.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.*;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@ActiveProfiles("test")
@SpringBootTest
public class AuthorizationServiceTest {

    @Mock
    private IsAuthorizedUseCase isAuthorized;

    @Autowired
    @InjectMocks
    private AuthorizationServiceImpl service;

    @BeforeEach
    void setupMocks() {
        openMocks(this);
    }

    @Test
    @DisplayName("[UNIT] | Should: verify if => is [AUTHORIZED]")
    void isAuthorized(){

        when(isAuthorized.execute()).thenReturn(true);

        service.isTransactionAuthorized(VALID_COMON_USER, new BigDecimal(10));

        verify(isAuthorized, times(1)).execute();
    }

}
