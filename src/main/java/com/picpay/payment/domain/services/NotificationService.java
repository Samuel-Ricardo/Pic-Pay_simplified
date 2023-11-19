package com.picpay.payment.domain.services;

import com.picpay.payment.domain.entities.user.User;

public interface NotificationService {
    void sendNotification(User user, String message) throws Exception;
}
