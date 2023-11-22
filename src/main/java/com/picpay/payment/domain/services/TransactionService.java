package com.picpay.payment.domain.services;

import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.entities.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    boolean validate(User sender, BigDecimal amount) throws Exception;

    Transaction transact(TransactionDTO dto) throws Exception;

    List<Transaction> findAll();
}
