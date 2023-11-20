package com.picpay.payment.domain.policy.transaction;

import com.picpay.payment.application.usecase.transaction.SaveTransactionUseCase;
import com.picpay.payment.domain.entities.transaction.Transaction;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface SendNotificationOnTransactPolicy {
    void execute(Transaction transaction) throws Exception;
}
