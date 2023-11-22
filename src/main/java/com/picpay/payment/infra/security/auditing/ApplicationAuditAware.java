package com.picpay.payment.infra.security.auditing;

import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ApplicationAuditAware implements AuditorAware<UUID> {

    @Autowired
    private AuthContextService context;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        return context.getUser().map(User::getId);
    }
}
