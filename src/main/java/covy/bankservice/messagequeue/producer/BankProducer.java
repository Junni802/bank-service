package covy.bankservice.messagequeue.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import covy.bankservice.dto.DepositNotificationDto;
import covy.bankservice.jpa.entity.Account;
import covy.bankservice.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void sendDepositNotification(Account account, Long depositAmount)
      throws JsonProcessingException {
    UserEntity user = account.getOwner();

    DepositNotificationDto notification = DepositNotificationDto.builder()
        .userId(user.getUserId())
        .email(user.getEmail())
        .accountNumber(account.getAccountNumber())
        .amount(depositAmount)
        .balance(account.getBalance())
        .message("입금이 완료되었습니다.")
        .build();

    String payload = objectMapper.writeValueAsString(notification);
    kafkaTemplate.send("bank.deposit.notifications", payload)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            System.out.println("❌ Kafka 전송 실패: 직렬화 오류");
            log.error("❌ Kafka 전송 실패: 직렬화 오류", ex);
          } else {
            System.out.println("💸 입금 알림 전송됨: {}");
            log.info("💸 입금 알림 전송됨: {}", payload);
          }
        });
  }
}
