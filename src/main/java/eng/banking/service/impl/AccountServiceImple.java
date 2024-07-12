package eng.banking.service.impl;

import eng.banking.entity.Account;
import eng.banking.entity.Customer;
import eng.banking.exception.AccountNotFound;
import eng.banking.repository.AccountRepository;
import eng.banking.repository.CustomerRepository;
import eng.banking.response.AccounResponse;
import eng.banking.request.AccountRequest;
import eng.banking.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImple implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    private final ModelMapper modelMapper;
    @Override
    public Account createAccount(AccountRequest accountRequest, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Account account = modelMapper.map(accountRequest, Account.class);
        account.setCustomer(customer);
        account.setCreatedAt(new Date());
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account updateAccount(Long accountId, AccountRequest accountRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound("Account not found"));
        Long originalId = account.getId();
        modelMapper.map(accountRequest, account);
        account.setId(originalId);
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound("Account not found"));
        accountRepository.delete(account);
    }

    @Override
    public Optional<AccounResponse> getAccountById(Long accountId) {

            Optional<Account> accountOptional = accountRepository.findById(accountId);

            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                AccounResponse response = modelMapper.map(account, AccounResponse.class);
                return Optional.of(response);
            } else {
                return Optional.empty();
            }
        }

    @Override
    public BigDecimal checkBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound("Account not found"));
        return account.getBalance();
    }
}
