package com.picpay.payment.domain.policy.security;

import java.util.function.Consumer;

public interface EncryptPasswordBeforeRegisteringUserPolicy {
    void execute(String password, Consumer<String> save);
}
