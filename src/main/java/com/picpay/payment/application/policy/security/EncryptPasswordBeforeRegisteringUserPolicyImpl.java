package com.picpay.payment.application.policy.security;

import com.picpay.payment.domain.policy.security.EncryptPasswordBeforeRegisteringUserPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class EncryptPasswordBeforeRegisteringUserPolicyImpl implements EncryptPasswordBeforeRegisteringUserPolicy {

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void execute(String password, Consumer<String> save) {
        save.accept(encoder.encode(password));
    }
}
