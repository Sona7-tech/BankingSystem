package eng.banking.controller;

import eng.banking.entity.Account;
import eng.banking.exception.AccountNotFound;
import eng.banking.repository.AccountRepository;
import eng.banking.response.AccounResponse;
import eng.banking.request.AccountRequest;
import eng.banking.service.impl.AccountServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImple accountServiceImple;
    private final AccountRepository accountRepository;
    @PostMapping("/create/{customerId}")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest accountRequest, @PathVariable Long customerId) {
        Account createdAccount = accountServiceImple.createAccount(accountRequest, customerId);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccounResponse> getAccountById(@PathVariable Long id) {
        Optional<AccounResponse> accountResponse = accountServiceImple.getAccountById(id);
        if (accountResponse.isPresent()) {
            return ResponseEntity.ok(accountResponse.get());
        } else {
            return ResponseEntity.notFound().build();
        }
        }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        accountServiceImple.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody AccountRequest accountDetails) {
        Account updatedAccount = accountServiceImple.updateAccount(accountId, accountDetails);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> checkBalance(@PathVariable Long accountId) {
        BigDecimal balance = accountServiceImple.checkBalance(accountId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

}
