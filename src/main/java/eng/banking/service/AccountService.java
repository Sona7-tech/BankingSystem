package eng.banking.service;

import eng.banking.entity.Account;
import eng.banking.response.AccounResponse;
import eng.banking.request.AccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest, Long customerId);

    Optional<AccounResponse> getAccountById(Long accountId, String authenticatedEmail);

    BigDecimal getAccountBalance(Long accountId, String authenticatedEmail);

}
