package org.example.push;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class PushNotificationService {

    private final PushService pushService;

    public PushNotificationService(
            @Value("${vapid.public}") String publicKey,
            @Value("${vapid.private}") String privateKey,
            @Value("${vapid.subject}") String subject
    ) throws Exception {
        pushService = new PushService(publicKey, privateKey, subject);
    }

    public void sendPush(String endpoint, String p256dh, String auth, String message) {
        try {
            Notification notification = new Notification(
                    endpoint,
                    p256dh,
                    auth,
                    message.getBytes(StandardCharsets.UTF_8)
            );
            pushService.send(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
