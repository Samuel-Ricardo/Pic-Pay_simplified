package com.picpay.payment.service.notification;

import com.picpay.payment.application.usecase.notification.SendNotificationUseCase;
import static com.picpay.payment.data.NotificationData.*;
import com.picpay.payment.data.UserData;
import com.picpay.payment.domain.dto.notification.NotificationDTO;
import com.picpay.payment.domain.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
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

    @Test
    @DisplayName("[UNIT] | Should: send => [NOTIFICATION]")
    void sendNotification() throws Exception {
        service.sendNotification(UserData.VALID_COMON_USER, "Message");
        verify(send, times(1)).execute(VALID_NOTIFICATION_DATA);
    }

}
