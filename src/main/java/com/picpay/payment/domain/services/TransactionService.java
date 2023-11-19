package com.picpay.payment.domain.services;

import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.user.User;

import java.math.BigDecimal;

public interface TransactionService {
    boolean validate(User sender, BigDecimal amount);

    TransactionService createTransaction(TransactionDTO dto);
}
