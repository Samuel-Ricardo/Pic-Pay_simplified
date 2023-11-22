package com.picpay.payment.application.usecase.transaction;

import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindAllTransactionsUseCase {

    @Autowired
    private TransactionRepository repository;

    public List<Transaction> execute() {
        return repository.findAll();
    }
}
