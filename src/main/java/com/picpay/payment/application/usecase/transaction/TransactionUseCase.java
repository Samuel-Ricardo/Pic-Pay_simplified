package com.picpay.payment.application.usecase.transaction;

import com.picpay.payment.domain.entities.transaction.Transaction;

public class TransactionUseCase {
    public Transaction execute(Transaction transaction){
        var sender = transaction.getSender();
        var receiver = transaction.getReceiver();

        sender
            .setBalance(
                sender
                    .getBalance()
                    .subtract(transaction.getAmount())
            );

        receiver
            .setBalance(
                receiver
                    .getBalance()
                    .add(transaction.getAmount()));

        return transaction;
    }
}
