package com.picpay.payment.application.policy.auth.token;

import com.picpay.payment.domain.policy.auth.token.TokenMustExpireInTwoHoursPolicy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;

@Component
public class TokenMustExpiresInTwoHoursPolicyImpl implements TokenMustExpireInTwoHoursPolicy {

    @Override
    public Instant execute(ZoneOffset offset) {
        return null;
    }

    @Override
    public Instant executeInBrazilOffset() {
        return null;
    }
}
