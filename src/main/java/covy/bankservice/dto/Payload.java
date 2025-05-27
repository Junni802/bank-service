package covy.bankservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

  private String id;
  private String account_number;
  private String balance;
  private int owner;
  private int unit_price;
  private int total_price;

}
