package com.picpay.payment.application.controllers;

import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody @Valid TransactionDTO transaction) throws Exception {
        return ResponseEntity
                .status(CREATED)
                .body(service.transact(transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
