package com.picpay.payment.domain.dto.error;

import java.util.Optional;

public record ExceptionDTO(
        String message,
        String statusCode
) {}
