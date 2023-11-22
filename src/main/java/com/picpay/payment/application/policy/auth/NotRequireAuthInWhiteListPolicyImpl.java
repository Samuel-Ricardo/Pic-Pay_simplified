package com.picpay.payment.application.policy.auth;

import com.picpay.payment.domain.policy.auth.NotRequireAuthInWhiteListPolicy;
import static com.picpay.payment.infra.config.security.SecurityConfig.WHITE_LIST;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class NotRequireAuthInWhiteListPolicyImpl implements NotRequireAuthInWhiteListPolicy {
    @Override
    public boolean execute(
            Runnable ignore,
            String URL
    ) {
        var empty = Arrays
                .stream(WHITE_LIST)
                .filter(URL::contains)
                .toList()
                .isEmpty();

        if (empty) ignore.run();
        return empty;
    }
}
