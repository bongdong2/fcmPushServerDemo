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
        "czHwBhS-Ik9Fozi-v2-FkX:APA91bEgx23HXyQqLmbmXRId00d8tOn9wNrv3VY4TC8WjceK_KZaoEoQu-kVmG-6ZyaZDKN9c0TjpQbYoJij8PTPGTQ63ijWxHkDDcfbDe4V8EX3CKVjkfOyDIxLekVBekdXhGtfqQCn",
        "caXJ-OTO1EdunOUADu_CjJ:APA91bFSUfLx6BDGPmyne8gSBlyi2lXtb5nYmh8IWToQR5N28mbDFK4bsS6jFpMtSgPV3c5kjdrXZ3Xz_rtYennfFrm3YMQmRRkRPKfzd4ZSllTD6Vu16jTLgPMaw4TdgRPAJELmV7Hf",
        "dYCFpfiVCkZHvZVpIvq2Np:APA91bESHsLp9WamN2bqWGMRD8jYr9aZOVmzlIo1e_5UwhOz_JGwB5EwgTDI2IqB6_PElXyDYWkMWItrGfjmYyY1TwVOgvq00ZCj648ybVaEKAcbW0usDsG5mJNcd7yskmVaNhJlfqRE",
        "fZGvG3Gk9ESKoh4-T3K8w4:APA91bHEIpo06ZXsBHGDK6z-bZe-0K0r2vcYwVLuFbAvPVlCNfJf1gTwC388ha2QrCf2Iuv1IGe1fDMmzBvnRga6WduGYwwZCKVYVa2-xg2AUwa5FM5m5FZV5_74fL-z0jFEuhWCtZsc",
        "e7MYEADjekKguo8VfiaCaK:APA91bEU-fZFJpNsfRATfs4eRLBOBBXJEzMqYsVCvYHW5eWtz6CErNZ14_RhOe8lleN5CeF6iZ6lJRkmN1_rmLu8FdVoW2Q374nj4PhWeJZw3KpoAheJvil3-znuPjrE-uE6hFkKQr9s",
        "ctYzPfn-nklVutUH6eBfuc:APA91bHBEIJM7fbruSdEbtOPOGUZIDdp3Wb-tLHhHoLuBlSAb_dJeKiUbhJY64wURmDWyJNnAUQPNm6Em_Wh12A44vBAkQxgeSRFBmP--N57zTBTfKaLOSsbFNRKJEnmEG33gD9w-fqi",
        "dAS6eJQ2ZEdTgZgQhvsTDT:APA91bFkMtgSrrqSWe5txwfvNZcLKX8gWtIFqp1X6mQqqf7kCx5uMzPPb3S-ZsviBiSnZUMjuK32lMCjq0n6sk6hgWLGZ2A5Gu8twCo3iOLfp37tt9Pbihab5_ecKVGfd3nn08yNZoDv",
        "e1ocbnc3S5mol3RyJiHUQh:APA91bHr2-FadKpO_uvpYUJ48nwNObpZz9fpFr1HPDOKOFr2WoADn0t9NgiJHiXNeKMRwuKFYDqXoVAG6IiQZ7pWp6OrfO0eWmhD06zIFpjmH8DSDttAefZilNDmrCTQoGUBzeclJL9n",
        "dQ3dZaf0TJy30oKAcs5T9F:APA91bGAnG5dW5ijtqG1D57vPe5WdHy4mRWr27LdFr6aJgG912_g8HuNe4mYYegseU2iNH6fp0LDK550lKNpurBIzG5OzKqbvIDmgqaUWATAymJau5wR5gLLDQVk_vZ4NgE22No0VpBD",
        "d9xudWcfAUQ1lM8uOe4dq_:APA91bFAZ6QNYqjoUlMTey6RhvrV7q8oBjrVD0w7sjzIvHZQtDoWZmkRtNYMTStzCzsT_omm8cjaux4ea_Gk-l96z2zwbL8xVPRpuR65PaoVxxfbu7IMa5IJRQJygNqx5rcJesUIgvFY"
    );

    MulticastMessage message = MulticastMessage.builder()
        .setNotification(Notification.builder().setTitle("오마이컴퍼니 앱이 출시되었습니다.").setBody("환영합니다.").build())
        .putData("score", "850")
        .putData("time", "2:45")
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
