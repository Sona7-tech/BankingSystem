package eng.banking.response;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDto {

    BigDecimal balance;
}
