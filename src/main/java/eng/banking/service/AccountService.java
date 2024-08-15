package eng.banking.service;

import eng.banking.entity.Account;
import eng.banking.response.AccounResponse;
import eng.banking.request.AccountRequest;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest, Long customerId);
    Account updateAccount(Long accountId, AccountRequest accountRequest);
    void deleteAccount(Long accountId);
    Optional<AccounResponse> getAccountById(Long accountId);

    BigDecimal checkBalance(Long accountId);

}
