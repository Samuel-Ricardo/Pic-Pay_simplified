package com.picpay.payment.application.service.auth;

import com.picpay.payment.application.usecase.auth.GetUserFromContextUseCase;
import com.picpay.payment.application.usecase.auth.SaveAuthContextUseCase;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.services.auth.AuthContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthContextServiceImpl implements AuthContextService {

    private final SaveAuthContextUseCase save;
    private final GetUserFromContextUseCase get;

    @Override
    public void saveContext(User user) {
        save.execute(user);
    }

    @Override
    public Optional<User> getUser() {
        return get.execute();
    }

    @Override
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
