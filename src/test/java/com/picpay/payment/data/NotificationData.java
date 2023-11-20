package com.picpay.payment.data;

import com.picpay.payment.domain.dto.notification.NotificationDTO;
import org.springframework.test.context.ActiveProfiles;
import static com.picpay.payment.data.UserData.VALID_COMON_USER;

@ActiveProfiles("test")
public class NotificationData {

    public static final NotificationDTO VALID_NOTIFICATION_DATA = new NotificationDTO(VALID_COMON_USER.getEmail(), "Message");

}
