package com.picpay.payment.domain.policy.auth.token;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface TokenMustExpireInTwoHours {
    Instant execute(ZoneOffset offset);
    Instant executeInBrazilOffset();
}
