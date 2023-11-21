package com.picpay.payment.domain.policy.auth.token;

import java.time.Instant;
import java.time.ZoneOffset;

public interface TokenMustExpireInTwoHoursPolicy {
    Instant execute(ZoneOffset offset);
    Instant executeInBrazilOffset();
}
