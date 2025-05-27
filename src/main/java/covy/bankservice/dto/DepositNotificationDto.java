package covy.bankservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepositNotificationDto {
  private String userId;
  private String email;
  private String accountNumber;
  private Long amount;
  private Long balance;
  private String message;
}
