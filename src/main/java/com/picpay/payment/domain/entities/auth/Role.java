package com.picpay.payment.domain.entities.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.picpay.payment.domain.entities.auth.Permissions.*;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER (
        Set.of(
                READ_USER,
                EXECUTE_TRANSACTION,
                READ_TRANSACTION
        )
    ),
    ADMIN(
            Stream.concat(
                    USER.permissions.stream(),
                    Set.of(
                            DELETE_USER,
                            CREATE_USER,
                            UPDATE_USER
                    ).stream()
            ).collect(Collectors.toSet())

    );

    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities.stream().toList();
    }
}
