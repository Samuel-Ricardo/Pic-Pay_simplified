package com.picpay.payment.application.service;

import com.picpay.payment.application.policy.transaction.MerchantUserCantTransactPolicyImpl;
import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.policy.transaction.CannotTransactWithoutSufficientBalancePolicy;
import com.picpay.payment.domain.policy.transaction.MerchantUserCantTransactPolicy;
import com.picpay.payment.domain.services.AuthorizationService;
import com.picpay.payment.domain.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private MerchantUserCantTransactPolicy merchantUserCantTransactPolicy;
    @Autowired
    private CannotTransactWithoutSufficientBalancePolicy cannotTransactWithoutSufficientBalancePolicy;


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
    public TransactionService createTransaction(TransactionDTO dto) {
        return null;
    }
}
