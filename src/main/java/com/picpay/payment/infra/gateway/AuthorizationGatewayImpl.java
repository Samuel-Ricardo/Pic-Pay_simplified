package com.picpay.payment.infra.gateway;

import com.picpay.payment.domain.gateways.AuthorizationGateway;
import com.picpay.payment.domain.gateways.generic.HttpGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Primary
@Component
public class AuthorizationGatewayImpl implements AuthorizationGateway {

    @Autowired
    protected HttpGateway client;

    @Value("${app.external.api.authorization.url}")
    private String URL;

    @Override
    public String getAuthorization(){
        var response = client.get(URL);
        if (response.getStatusCode() != HttpStatus.OK) return "";

        return (String) Objects.requireNonNull(response.getBody()).get("message");
    }
}
