package com.picpay.payment.infra.security.auditing;

import com.picpay.payment.domain.entities.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class ApplicationAuditAware implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if(
                authentication == null ||
               !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken
        ) { return Optional.empty(); }

        User user = (User) authentication.getPrincipal();
        return Optional.ofNullable(user.getId());
    }
}
