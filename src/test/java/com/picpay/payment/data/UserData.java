package com.picpay.payment.data;

import com.picpay.payment.domain.dto.user.UserDTO;
import com.picpay.payment.domain.entities.user.User;
import org.springframework.test.context.ActiveProfiles;

import static com.picpay.payment.domain.entities.auth.Role.*;
import static com.picpay.payment.domain.entities.user.UserType.*;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.random.RandomGenerator;

@ActiveProfiles("test")
public class UserData {

    public static final UserDTO VALID_COMMON_USER_DATA = new UserDTO(
                "User",
                "Common",
                "12345678901",
                new BigDecimal(1000),
                "common@mail.com",
                "123456",
                COMMON,
                USER
            );

    public static final User VALID_COMON_USER = User.from(VALID_COMMON_USER_DATA);

    public static UserDTO RANDOM_VALID_COMMON_USER_DATA() {
        return new UserDTO(
                Math.random()+"",
                Math.random()+"",
                Math.random()+"",
                new BigDecimal(2000),
                Math.random()+"@mail.com",
                Math.random()+"",
                COMMON,
                USER
        );
    }

    public static User RANDOM_VALID_COMMON_USER() {
        var user = User.from(RANDOM_VALID_COMMON_USER_DATA());
        user.setId(UUID.randomUUID());
        return user;
    }

    public static final UserDTO VALID_MERCHANT_USER_DATA = new UserDTO(
            "User2",
            "Merchant",
            "12345678902",
            new BigDecimal(2000),
            "merchant@mail.com",
            "123456",
            MERCHANT,
            USER
        );

    public static final User VALID_MERCHANT_USER = User.from(VALID_MERCHANT_USER_DATA);

}
