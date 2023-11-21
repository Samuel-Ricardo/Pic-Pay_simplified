package com.picpay.payment.domain.entities.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {
    READ_USER("READ:USER"),
    CREATE_USER("CREATE:USER"),
    DELETE_USER("DELETE:USER"),
    UPDATE_USER("UPDATE:USER"),

    READ_TRANSACTION("READ:TRANSACTION"),
    EXECUTE_TRANSACTION("CREATE:TRANSACTION");

    private final String permission;
}

