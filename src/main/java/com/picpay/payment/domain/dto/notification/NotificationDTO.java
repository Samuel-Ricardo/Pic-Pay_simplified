package com.picpay.payment.domain.dto.notification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NotificationDTO(
   @NotBlank
   @Email String email,
   @NotBlank String message
) {}
