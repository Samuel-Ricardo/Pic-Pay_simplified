package com.picpay.payment.service.user;

import com.picpay.payment.application.service.UserServiceImpl;
import com.picpay.payment.application.usecase.user.FindAllUsersUseCase;
import com.picpay.payment.application.usecase.user.FindUserByIdUseCase;
import com.picpay.payment.application.usecase.user.SaveUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

    @Mock
    private FindUserByIdUseCase findById;
    @Mock
    private FindAllUsersUseCase findAll;
    @Mock
    private SaveUserUseCase save;

    @Autowired
    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setup() {
        openMocks(this);
    }


}
