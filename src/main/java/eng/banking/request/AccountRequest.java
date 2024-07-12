package eng.banking.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {

    String accountNumber;
    BigDecimal balance;
    Long customerId;

}
