package com.picpay.payment.infra.gateway;

import com.picpay.payment.domain.dto.notification.NotificationDTO;
import com.picpay.payment.domain.entities.user.User;
import com.picpay.payment.domain.gateways.NotificationGateway;
import com.picpay.payment.domain.gateways.generic.HttpGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Primary
@Component
public class NotificationGatewayImpl implements NotificationGateway {

    @Autowired
    protected HttpGateway client;

    @Value("${app.external.api.notification.url}")
    protected String URL;

    @Override
    public void sendNotification(NotificationDTO notification) throws Exception {

        var response = client.post(URL, notification);
        if(response.getStatusCode() != HttpStatus.OK) throw new Exception("Notification Service not working");

        System.out.println("Send notification to User | "+notification.toString());
    }
}
