package com.picpay.payment.domain.dto.error;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record ExceptionDTO(
        @NotBlank String message,
        @NotBlank String statusCode
) {}
