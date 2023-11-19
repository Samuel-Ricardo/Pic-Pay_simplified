package com.picpay.payment.application.usecase.notification;

import com.picpay.payment.domain.dto.notification.NotificationDTO;
import com.picpay.payment.domain.gateways.NotificationGateway;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationUseCase {
    protected NotificationGateway gateway;

    public void execute(NotificationDTO notification) throws Exception {
        gateway.sendNotification(notification);
    }
}
