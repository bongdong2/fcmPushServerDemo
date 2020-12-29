package com.ohmycompany.notification.demo;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMService {
  public void send(final NotificationRequest notificationRequest) throws InterruptedException, ExecutionException {
    Message message = Message.builder()
        .setToken(notificationRequest.getToken())
        .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
            .setNotification(new WebpushNotification(notificationRequest.getTitle(),
                notificationRequest.getMessage()))
            .build())
        .build();

    String response = FirebaseMessaging.getInstance().sendAsync(message).get();
    log.info("Sent message: " + response);
  }
}
