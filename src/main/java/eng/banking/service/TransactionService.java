package eng.banking.service;

import eng.banking.entity.Transaction;
import eng.banking.entity.TransactionType;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

     Transaction createTransaction(Long accountId, TransactionType type, BigDecimal amount, Authentication authentication);

     List<Transaction> getTransactionHistory(Long accountId, Authentication authentication);
}
