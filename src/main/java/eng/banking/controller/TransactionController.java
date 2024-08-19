package eng.banking.controller;

import eng.banking.entity.Transaction;
import eng.banking.entity.TransactionType;
import eng.banking.exception.InsufficientBalanceException;
import eng.banking.exception.TransferException;
import eng.banking.service.impl.TransactionServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TransactionController {

private final TransactionServiceImple transactionServiceImple;

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam Long accountId, @RequestParam BigDecimal amount, Authentication authentication) {
        try {
            Transaction transaction = transactionServiceImple.createTransaction(accountId, TransactionType.DEPOSIT, amount, authentication);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (AccessDeniedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access Denied");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (TransferException | InsufficientBalanceException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Transaction Failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal Server Error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount, Authentication authentication) {
        try {
            Transaction transaction = transactionServiceImple.createTransaction(accountId, TransactionType.WITHDRAWAL, amount, authentication);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (AccessDeniedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access Denied");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (TransferException | InsufficientBalanceException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Transaction Failed");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal Server Error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam BigDecimal amount, Authentication authentication) {

       try {
        Transaction withdrawalTransaction = transactionServiceImple.createTransaction(fromAccountId, TransactionType.WITHDRAWAL, amount, authentication);
        return new ResponseEntity<>(withdrawalTransaction, HttpStatus.CREATED);

    } catch (AccessDeniedException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Access Denied");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    } catch (TransferException | InsufficientBalanceException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Transaction Failed");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception e) {
        e.printStackTrace();
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    }

        @GetMapping("/history")
        public ResponseEntity<?> getTransactionHistory(@RequestParam Long accountId, Authentication authentication) {

            try {
                List<Transaction> transactions = transactionServiceImple.getTransactionHistory(accountId, authentication);
                return ResponseEntity.ok(transactions);
            } catch (AccessDeniedException e) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Access Denied");
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> response = new HashMap<>();
                response.put("error", "Internal Server Error");
                response.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }
    }
    
