package covy.bankservice.messagequeue.producer;

import covy.bankservice.jpa.entity.Account;
import covy.bankservice.jpa.entity.UserEntity;
import covy.bankservice.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka(partitions = 1, topics = "bank.deposit.notifications")
@SpringBootTest
class DepositNotificationServiceTest {

  @Autowired
  private BankProducer bankProducer;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testDepositNotificationMessageSent() throws Exception {

    UserEntity user = userRepository.findByUserId("16468118-ee59-4101-9709-90edbdb7b240");



    // given
    Account account = new Account(1L, "test1234", 1L, user, "COVY");
    Long amount = 10000L;

    // when
    bankProducer.sendDepositNotification(account, amount);
  }
}
