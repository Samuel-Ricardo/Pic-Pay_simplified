package com.picpay.payment.application.policy.security;

import com.picpay.payment.domain.policy.security.EncryptPasswordBeforeRegisteringUserPolicy;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class EncryptPasswordBeforeRegisteringUserPolicyImpl implements EncryptPasswordBeforeRegisteringUserPolicy {
    @Override
    public void execute(String password, Consumer<String> save) {
        
    }
}
