package eng.banking.service.impl;

import eng.banking.entity.Account;
import eng.banking.entity.Customer;
import eng.banking.repository.AccountRepository;
import eng.banking.repository.CustomerRepository;
import eng.banking.response.AccounResponse;
import eng.banking.request.AccountRequest;
import eng.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.security.access.AccessDeniedException;
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
    public Optional<AccounResponse> getAccountById(Long accountId, String authenticatedEmail) {

        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

            Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (account.getCustomer().getId().equals(authenticatedCustomer.getId())) {

                AccounResponse response = modelMapper.map(account, AccounResponse.class);
                return Optional.of(response);
            } else {
                throw new AccessDeniedException("You do not have permission to view this account.");
            }
        } else {
            return Optional.empty();
        }
        }

    @Override
    public BigDecimal getAccountBalance(Long accountId, String authenticatedEmail) {

        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getCustomer().getId().equals(authenticatedCustomer.getId())) {
            throw new AccessDeniedException("You do not have permission to view this account's balance.");
        }

        return account.getBalance();
    }


}
