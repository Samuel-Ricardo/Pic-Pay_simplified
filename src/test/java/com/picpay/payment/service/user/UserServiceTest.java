package com.picpay.payment.service.user;

import com.picpay.payment.application.service.UserServiceImpl;
import com.picpay.payment.application.usecase.user.FindAllUsersUseCase;
import com.picpay.payment.application.usecase.user.FindUserByIdUseCase;
import com.picpay.payment.application.usecase.user.SaveUserUseCase;
import static com.picpay.payment.data.UserData.*;
import com.picpay.payment.domain.dto.user.UserDTO;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.openMocks;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class UserServiceTest {

    @Mock private FindUserByIdUseCase findById;
    @Mock private FindAllUsersUseCase findAll;
    @Mock private SaveUserUseCase save;

    @Autowired
    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setup() {
        openMocks(this);
    }


    @Test
    @DisplayName("[UNIT] | Should: save => [USER]")
    void saveUser(){
        when(save.execute(any(VALID_COMON_USER.getClass())))
                .thenReturn(VALID_COMON_USER);

        var result = service.saveUser(VALID_COMMON_USER_DATA);

        verify(save, times(1)).execute(any(VALID_COMON_USER.getClass()));
        assertEquals(VALID_COMON_USER, result);
    }

    
}
