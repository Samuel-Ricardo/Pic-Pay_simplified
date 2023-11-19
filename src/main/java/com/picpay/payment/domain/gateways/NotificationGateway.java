package com.picpay.payment.domain.gateways;

import com.picpay.payment.domain.dto.notification.NotificationDTO;

public interface NotificationGateway {
    void sendNotification(NotificationDTO notification) throws Exception;
}
