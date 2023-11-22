package com.picpay.payment.domain.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionDTO(
        @NotNull BigDecimal value,
        @NotNull UUID senderId,
        @NotNull UUID receiverId
    ) { }
