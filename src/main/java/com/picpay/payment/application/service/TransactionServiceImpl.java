package com.picpay.payment.application.service;

import com.picpay.payment.application.policy.transaction.MerchantUserCantTransactPolicyImpl;
import com.picpay.payment.application.usecase.transaction.FindAllTransactionsUseCase;
import com.picpay.payment.application.usecase.transaction.SaveTransactionUseCase;
import com.picpay.payment.application.usecase.transaction.TransactionUseCase;
import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.policy.transaction.CannotTransactWithoutSufficientBalancePolicy;
import com.picpay.payment.domain.policy.transaction.MerchantUserCantTransactPolicy;
import com.picpay.payment.domain.policy.transaction.SendNotificationOnTransactPolicy;
import com.picpay.payment.domain.services.AuthorizationService;
import com.picpay.payment.domain.services.TransactionService;
import com.picpay.payment.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserService userService;

    @Autowired
    private MerchantUserCantTransactPolicy merchantUserCantTransactPolicy;
    @Autowired
    private CannotTransactWithoutSufficientBalancePolicy cannotTransactWithoutSufficientBalancePolicy;
    @Autowired
    private SendNotificationOnTransactPolicy sendNotificationOnTransactPolicy;

    @Autowired
    private TransactionUseCase transct;
    @Autowired
    private SaveTransactionUseCase save;
    @Autowired
    private FindAllTransactionsUseCase findAllTransactions;

    @Override
    public boolean validate(User sender, BigDecimal amount) throws Exception {

        if(merchantUserCantTransactPolicy.execute(sender.getUserType()))
            throw new Exception("Merchant User is not authorized to transact");

        if(cannotTransactWithoutSufficientBalancePolicy.execute(sender, amount))
            throw new Exception("Balance is not enough");

        if(!authorizationService.isTransactionAuthorized(sender, amount))
            throw new Exception("Transaction Not Authorized, try again later...");

        return true;
    }

    @Override
    public Transaction transact(TransactionDTO dto) throws Exception {

        var sender = userService.findUserById(dto.senderId());
        this.validate(sender, dto.value());
        var receiver = userService.findUserById(dto.receiverId());

        var transaction = new Transaction();

        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(dto.value());

        transaction = transct.execute(transaction);
        transaction = save.execute(transaction);

        userService.saveUser(transaction.getSender());
        userService.saveUser(transaction.getReceiver());

        sendNotificationOnTransactPolicy.execute(transaction);

        return transaction;
    }

    @Override
    public List<Transaction> findAll() {
        return findAllTransactions.execute();
    }
}
