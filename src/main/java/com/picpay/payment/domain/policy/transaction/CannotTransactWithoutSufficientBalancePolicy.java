package com.picpay.payment.domain.policy.transaction;

import com.picpay.payment.domain.entities.user.User;

import java.math.BigDecimal;

public interface CannotTransactWithoutSufficientBalancePolicy {
    boolean execute(User sender, BigDecimal amount);
}
