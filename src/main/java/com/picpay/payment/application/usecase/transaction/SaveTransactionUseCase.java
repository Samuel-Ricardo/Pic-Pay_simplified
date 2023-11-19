package com.picpay.payment.application.usecase.transaction;

import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveTransactionUseCase {

    @Autowired
    private TransactionRepository repository;

    public Transaction execute(Transaction transaction){
        return repository.save(transaction);
    }
}
