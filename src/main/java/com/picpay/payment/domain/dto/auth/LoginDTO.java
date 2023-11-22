package com.picpay.payment.domain.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record LoginDTO(
        @NotBlank
        @Email String email,
        @NotBlank
        @Size(min = 3) String password
) {}
