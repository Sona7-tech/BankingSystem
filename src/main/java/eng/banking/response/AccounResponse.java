package eng.banking.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccounResponse {

    String accountNumber;
    BigDecimal balance;
    Long customerId;
    Date createdAt;

}
