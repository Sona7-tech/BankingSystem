package eng.banking.service.impl;

import eng.banking.entity.Account;
import eng.banking.entity.Customer;
import eng.banking.entity.Transaction;
import eng.banking.entity.TransactionType;
import eng.banking.exception.AccountNotFound;
import eng.banking.exception.InsufficientBalanceException;
import eng.banking.exception.NoTransactionsFoundException;
import eng.banking.exception.TransferException;
import eng.banking.repository.AccountRepository;
import eng.banking.repository.CustomerRepository;
import eng.banking.repository.TransactionRepository;
import eng.banking.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImple implements TransactionService {

     private final TransactionRepository transactionRepository;
     private final AccountRepository accountRepository;

     private final CustomerRepository customerRepository;


    @Override
    @Transactional
    public Transaction createTransaction(Long accountId, TransactionType type, BigDecimal amount, Authentication authentication) {
        if (amount.compareTo(BigDecimal.ONE) < 0) {
            throw new TransferException("Minimum amount should be 0.01 $.");
        }
        String authenticatedEmail = authentication.getName();


        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound("Account not found"));

        if (!account.getCustomer().getId().equals(authenticatedCustomer.getId())) {
            throw new AccessDeniedException("You do not have permission to access this account.");
        }

        if (type == TransactionType.WITHDRAWAL && account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Max amount should be : " + account.getBalance() + "$");
        }


        if (type == TransactionType.DEPOSIT) {
            account.setBalance(account.getBalance().add(amount));
        } else if (type == TransactionType.WITHDRAWAL) {
            account.setBalance(account.getBalance().subtract(amount));
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(type);
        transaction.setAmount(amount);
        transaction.setTransactionDate(new Date());

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionHistory(Long accountId, Authentication authentication) {

        String authenticatedEmail = authentication.getName();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFound("Account with ID " + accountId + " not found."));

        Customer authenticatedCustomer = customerRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!account.getCustomer().getId().equals(authenticatedCustomer.getId())) {
            throw new AccessDeniedException("You do not have permission to view this account's transaction history.");
        }

        List<Transaction> transactionList = transactionRepository.findByAccountId(accountId);
        if (transactionList.isEmpty()) {
            throw new NoTransactionsFoundException("No transactions found for account ID " + accountId);
        }

        return transactionList;
    }


}

