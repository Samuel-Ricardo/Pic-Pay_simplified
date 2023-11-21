package com.picpay.payment.application.policy.auth.token;

import com.picpay.payment.domain.policy.auth.token.TokenMustExpireInTwoHoursPolicy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TokenMustExpiresInTwoHoursPolicyImpl implements TokenMustExpireInTwoHoursPolicy {

    @Override
    public Instant execute(ZoneOffset offset) {
        return LocalDateTime.now().plusHours(2).toInstant(offset);
    }

    @Override
    public Instant executeInBrazilOffset() {
        return this.execute(ZoneOffset.of("-03:00"));
    }
}
