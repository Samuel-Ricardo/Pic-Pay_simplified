package com.picpay.payment.data;

import com.picpay.payment.domain.dto.transaction.TransactionDTO;
import com.picpay.payment.domain.entities.transaction.Transaction;
import com.picpay.payment.domain.entities.user.User;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.picpay.payment.data.UserData.*;

@ActiveProfiles("test")
public class TransactionData {

    public static final User SENDER = RANDOM_VALID_COMMON_USER();
    public static final User RECEIVER = RANDOM_VALID_COMMON_USER();

    public static final TransactionDTO VALID_TRANSACTION_DATA = new TransactionDTO(
            new BigDecimal(100),
            SENDER.getId(),
            RECEIVER.getId()
    );

    public static final Transaction VALID_TRANSACTION = new Transaction(
            UUID.randomUUID(),
            new BigDecimal(1000),
            SENDER,
            RECEIVER,
            LocalDateTime.now()
    );

}
