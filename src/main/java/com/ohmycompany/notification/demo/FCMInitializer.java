package com.ohmycompany.notification.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FCMInitializer {
  private static final String FIREBASE_CONFIG_PATH = "firebase/ohmycompany-cf284-firebase-adminsdk-u1t2n-871d980d7c.json";

  @PostConstruct
  public void initialize() {
    try {
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())).build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
        log.info("Firebase application has been initialized");
      }
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }
}
