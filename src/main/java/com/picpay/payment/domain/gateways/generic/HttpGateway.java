package com.picpay.payment.domain.gateways.generic;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface HttpGateway {

    ResponseEntity<Map> get(String URL);

    ResponseEntity<Map> post(String URL, Object data);

    ResponseEntity<Map> put(String URL, Object data);

    ResponseEntity<Map> patch(String URL, Object data);

    ResponseEntity<Map> delete(String URL);

}
