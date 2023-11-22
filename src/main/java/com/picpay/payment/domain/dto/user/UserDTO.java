package com.picpay.payment.domain.dto.user;

import com.picpay.payment.domain.entities.auth.Role;
import com.picpay.payment.domain.entities.user.UserType;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UserDTO(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String document,
        @NotBlank
        BigDecimal balance,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        UserType userType,
        @NotBlank
        Role role
) {
}
