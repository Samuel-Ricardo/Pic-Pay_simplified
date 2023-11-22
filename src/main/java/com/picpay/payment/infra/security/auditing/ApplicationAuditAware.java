package com.picpay.payment.infra.security.auditing;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ApplicationAuditAware implements AuditorAware<UUID> {

    private final AuthContextService context;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return context.getUser().map(User::getId);
    }
}
