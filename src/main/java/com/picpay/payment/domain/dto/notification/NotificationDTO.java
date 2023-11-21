package com.picpay.payment.domain.dto.notification;

import jakarta.validation.constraints.NotBlank;

public record NotificationDTO(
   @NotBlank String email,
   @NotBlank String message
) {}
