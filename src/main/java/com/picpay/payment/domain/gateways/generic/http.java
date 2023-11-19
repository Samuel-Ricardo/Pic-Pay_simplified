package com.picpay.payment.domain.gateways.generic;

import org.springframework.http.ResponseEntity;

public interface http {

    ResponseEntity<Object> get(String URL);

    ResponseEntity<Object> post(String URL, Object data);

    ResponseEntity<Object> put(String URL, Object data);

    ResponseEntity<Object> patch(String URL, Object data);

    ResponseEntity<Object> delete(String URL);

}
