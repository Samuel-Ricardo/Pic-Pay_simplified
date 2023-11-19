package com.picpay.payment.application.service;

import com.picpay.payment.application.usecase.notification.SendNotificationUseCase;
import com.picpay.payment.domain.dto.notification.NotificationDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SendNotificationUseCase send;

    @Override
    public void sendNotification(User user, String message) throws Exception {
        var notification = new NotificationDTO(user.getEmail(), message);
        this.send.execute(notification);
    }
}
