package com.picpay.payment.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank String email,
        @NotBlank   String password
) {}
