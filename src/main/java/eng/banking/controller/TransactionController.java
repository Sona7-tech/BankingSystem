package eng.banking.controller;

import eng.banking.entity.Transaction;
import eng.banking.entity.TransactionType;
import eng.banking.service.impl.TransactionServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {

private final TransactionServiceImple transactionServiceImple;

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        Transaction transaction = transactionServiceImple.createTransaction(accountId, TransactionType.DEPOSIT, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        Transaction transaction = transactionServiceImple.createTransaction(accountId, TransactionType.WITHDRAWAL, amount);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam BigDecimal amount) {
        Transaction withdrawalTransaction = transactionServiceImple.createTransaction(fromAccountId, TransactionType.WITHDRAWAL, amount);
        //Transaction depositTransaction = transactionServiceImple.createTransaction(toAccountId, TransactionType.DEPOSIT, amount);

        return new ResponseEntity<>(withdrawalTransaction, HttpStatus.CREATED);
    }


    @GetMapping("/history")
    public List<Transaction> getTransactionHistory(@RequestParam Long accountId) {

        return transactionServiceImple.getTransactionHistory(accountId);

    }

}
