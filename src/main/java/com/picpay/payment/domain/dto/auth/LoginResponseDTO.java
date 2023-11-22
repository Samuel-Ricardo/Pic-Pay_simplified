package com.picpay.payment.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDTO(
        @NotBlank String token
) {}
