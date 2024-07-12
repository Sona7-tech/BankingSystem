package eng.banking.service;

import eng.banking.entity.Transaction;
import eng.banking.entity.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

     Transaction createTransaction(Long accountId, TransactionType type, BigDecimal amount);

     List<Transaction> getTransactionHistory(Long accountId);
}
