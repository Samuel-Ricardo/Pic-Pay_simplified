package com.picpay.payment.service.notification;

import com.picpay.payment.application.usecase.notification.SendNotificationUseCase;
import com.picpay.payment.domain.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class NotificationServiceTest {

    @Mock
    private SendNotificationUseCase send;

    @Autowired
    @InjectMocks
    private NotificationService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    

}
