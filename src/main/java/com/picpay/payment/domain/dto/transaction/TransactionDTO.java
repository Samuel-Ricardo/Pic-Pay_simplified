package com.picpay.payment.domain.dto.transaction;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(
        @NotBlank BigDecimal value,
        @NotBlank UUID senderId,
        @NotBlank UUID receiverId
    ) { }
