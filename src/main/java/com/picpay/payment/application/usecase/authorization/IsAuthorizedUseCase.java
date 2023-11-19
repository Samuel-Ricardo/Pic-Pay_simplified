package com.picpay.payment.application.usecase.authorization;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.gateways.AuthorizationGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IsAuthorizedUseCase {
    @Autowired
    private AuthorizationGateway gateway;

    public boolean execute() {
        return gateway
                .getAuthorization()
                .equalsIgnoreCase("Autorizado");
    }
}
