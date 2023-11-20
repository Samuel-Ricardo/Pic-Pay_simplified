package com.picpay.payment.application.policy.transaction;

import com.picpay.payment.application.usecase.transaction.SaveTransactionUseCase;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.policy.transaction.SendNotificationOnTransactPolicy;
import com.picpay.payment.domain.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendNotificattionOnTransactPolicyImpl implements SendNotificationOnTransactPolicy {

    @Autowired
    private NotificationService service;

    @Override
    public void execute(Transaction transaction) throws Exception {
        service.sendNotification(transaction.getSender(), "Transaction realized with success");
        service.sendNotification(transaction.getReceiver(), "Transaction received with success");
    }
}
