package com.picpay.payment.infra.gateway.generic;

import com.picpay.payment.domain.gateways.generic.HttpGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Primary
@Component
public class HttpGatewayImpl implements HttpGateway {

    @Autowired
    protected RestTemplate client;

    @Override
    public ResponseEntity<Map> get(String URL)  {
        return client.getForEntity(URL, Map.class);
    }

    @Override
    public ResponseEntity<Map> post(String URL, Object data) {
        return client.postForEntity(URL, data, Map.class);
    }

    @Override
    public ResponseEntity<Map> put(String URL, Object data) {
        return  null;
    }

    @Override
    public ResponseEntity<Map> patch(String URL, Object data) {
        return null;
    }

    @Override
    public ResponseEntity<Map> delete(String URL) {
        return null;
    }
}
