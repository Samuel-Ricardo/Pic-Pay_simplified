package com.picpay.payment.domain.gateways;

import org.springframework.beans.factory.annotation.Value;

public interface AuthorizationGateway {
    String getAuthorization() throws Exception;
}
