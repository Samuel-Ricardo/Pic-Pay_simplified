package com.picpay.payment.policy.transaction;

import static com.picpay.payment.data.TransactionData.*;
import static org.junit.jupiter.api.Assertions.*;

import com.picpay.payment.application.policy.transaction.SendNotificationOnTransactPolicyImpl;
import com.picpay.payment.application.service.NotificationServiceImpl;
import com.picpay.payment.domain.dto.notification.NotificationDTO;
import com.picpay.payment.domain.gateways.NotificationGateway;
import com.picpay.payment.domain.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import static org.mockito.MockitoAnnotations.initMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class SendNotificationOnTransactPolicyTest {

    @Mock
    private NotificationServiceImpl service;

    @Autowired
    @InjectMocks
    private SendNotificationOnTransactPolicyImpl policy;

    @BeforeEach
    void setup(){
        initMocks(this);
    }

    @Test
    @DisplayName("[UNIT] | Should: Send => [NOTIFICATION] on execute => [TRANSACTION]")
    void executePolicy() throws Exception {

        assertDoesNotThrow(() -> policy.execute(VALID_TRANSACTION));

        verify(service, times(1)).sendNotification(VALID_TRANSACTION.getSender(), "Transaction realized with success");
        verify(service, times(1)).sendNotification(VALID_TRANSACTION.getReceiver(), "Transaction received with success");
    }

}
