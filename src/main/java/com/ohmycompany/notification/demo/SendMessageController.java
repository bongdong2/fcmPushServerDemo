package com.ohmycompany.notification.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SendMessageController {

  //private final FirebaseCloudMessageService firebaseCloudMessageService;

  private final FCMInitializer fcmInitializer;
  private final FCMService fcmService;

  @GetMapping("/single/user/message")
  public void multiUserMessage() throws ExecutionException, InterruptedException {
    fcmInitializer.initialize();

    NotificationRequest request = NotificationRequest.builder()
        .token("caXJ-OTO1EdunOUADu_CjJ:APA91bFSUfLx6BDGPmyne8gSBlyi2lXtb5nYmh8IWToQR5N28mbDFK4bsS6jFpMtSgPV3c5kjdrXZ3Xz_rtYennfFrm3YMQmRRkRPKfzd4ZSllTD6Vu16jTLgPMaw4TdgRPAJELmV7Hf")
        .title("hello")
        .message("seungui")
        .build();

    fcmService.send(request);
  }

  @GetMapping("/multi/message")
  public void multicastMessage() throws FirebaseMessagingException, ExecutionException, InterruptedException {
    fcmInitializer.initialize();

    List<String> registrationTokens = Arrays.asList(
        "dMK9chPFTNmUqA4HYzyQIu:APA91bEZM65YIlQjlGUoqrfl2l55o0shQnOoTtHmnoNFMCjcM67unh5B7Juq1kmebL03rf5E0okXClpmN4vy23AZ8ITefVQSgfdqKdezYc6z-qovf_829HNtSzjjvL68fGIxNWWUXb3Y"
    );

    MulticastMessage message = MulticastMessage.builder()
        .setNotification(Notification.builder().setTitle("오마이컴퍼니 앱이 출시되었습니다.").setBody("환영합니다.").build())
        .putData("link", "https://www.ohmycompany.com/mypage/account")
        .addAllTokens(registrationTokens)
        .build();
    BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
    if (response.getFailureCount() > 0) {
      List<SendResponse> responses = response.getResponses();
      List<String> failedTokens = new ArrayList<>();
      for (int i = 0; i < responses.size(); i++) {
        if (!responses.get(i).isSuccessful()) {
          // The order of responses corresponds to the order of the registration tokens.
          failedTokens.add(registrationTokens.get(i));
        }
      }

      System.out.println("List of tokens that caused failures: " + failedTokens);
    }
  }
}
