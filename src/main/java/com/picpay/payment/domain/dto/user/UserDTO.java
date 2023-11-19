package com.picpay.payment.domain.dto.user;

import com.picpay.payment.domain.entities.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
}
