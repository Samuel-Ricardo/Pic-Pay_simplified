package com.picpay.payment.application.service.auth;

import com.picpay.payment.domain.services.auth.AuthContextService;
import com.picpay.payment.domain.services.auth.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private AuthContextService context;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        context.
    }
}
