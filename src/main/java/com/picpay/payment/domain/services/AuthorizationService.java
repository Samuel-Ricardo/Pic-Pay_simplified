package com.picpay.payment.domain.services;

import com.picpay.payment.domain.entities.user.User;

import java.math.BigDecimal;

public interface AuthorizationService {
    boolean isTransactionAuthorized(User sender, BigDecimal value);
}
