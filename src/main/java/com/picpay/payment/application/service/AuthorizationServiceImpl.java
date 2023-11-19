package com.picpay.payment.application.service;

import com.picpay.payment.application.usecase.authorization.IsAuthorizedUseCase;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.gateways.AuthorizationGateway;
import com.picpay.payment.domain.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private IsAuthorizedUseCase isAuthorized;

    @Override
    public boolean isTransactionAuthorized(User sender, BigDecimal value) {
        return this.isAuthorized.execute();
    }
}
