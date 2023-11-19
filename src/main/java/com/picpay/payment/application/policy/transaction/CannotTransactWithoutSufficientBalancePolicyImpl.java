package com.picpay.payment.application.policy.transaction;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.policy.transaction.CannotTransactWithoutSufficientBalancePolicy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CannotTransactWithoutSufficientBalancePolicyImpl implements CannotTransactWithoutSufficientBalancePolicy {
    @Override
    public boolean execute(User sender, BigDecimal amount) {
        return sender.getBalance().compareTo(amount) < 0;
    }
}
