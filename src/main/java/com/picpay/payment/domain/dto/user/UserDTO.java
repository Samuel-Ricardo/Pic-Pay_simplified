package com.picpay.payment.domain.dto.user;

import com.picpay.payment.domain.entities.auth.Role;
import com.picpay.payment.domain.entities.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UserDTO(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String document,
        @NotNull
        BigDecimal balance,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 3)
        String password,
        @NotNull
        UserType userType,
        @NotNull
        Role role
) {
}
