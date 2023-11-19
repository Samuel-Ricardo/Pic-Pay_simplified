package com.picpay.payment.domain.repositories;

import com.picpay.payment.domain.entities.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {}
