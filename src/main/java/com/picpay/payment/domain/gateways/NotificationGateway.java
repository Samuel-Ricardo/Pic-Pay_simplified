package com.picpay.payment.domain.gateways;

import com.picpay.payment.domain.entities.user.User;

public interface NotificationGateway {
    void sendNotification(User user, String message) throws Exception;
}
