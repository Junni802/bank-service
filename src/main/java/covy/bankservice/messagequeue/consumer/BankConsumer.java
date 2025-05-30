package covy.bankservice.messagequeue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import covy.bankservice.dto.DepositNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankConsumer {

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "bank.deposit.notifications", groupId = "bank-consumer-group")
  public void listen(ConsumerRecord<String, String> record) {
    String message = record.value();

    try {
      DepositNotificationDto notification = objectMapper.readValue(message, DepositNotificationDto.class);
      log.info("💬 입금 알림 수신: {}", notification);

      // TODO: 알림 로직 처리 (예: 이메일 발송, DB 저장 등)

    } catch (Exception e) {
      log.error("❌ 입금 알림 파싱 실패: {}", message, e);
    }
  }
}

