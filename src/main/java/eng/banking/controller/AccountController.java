package eng.banking.controller;

import eng.banking.entity.Account;
import eng.banking.response.AccounResponse;
import eng.banking.request.AccountRequest;
import eng.banking.service.impl.AccountServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImple accountServiceImple;

    @PostMapping("/create/{customerId}")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest accountRequest, @PathVariable Long customerId) {
        Account createdAccount = accountServiceImple.createAccount(accountRequest, customerId);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id, Authentication authentication) {
        try {
            String authenticatedEmail = authentication.getName();
            Optional<AccounResponse> accountResponse = accountServiceImple.getAccountById(id, authenticatedEmail);

            if (accountResponse.isPresent()) {

                return ResponseEntity.ok(accountResponse.get());
            } else {
                // Returning a 404 status with a custom error message
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Account Not Found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (AccessDeniedException e) {
            // Returning a 403 status with an access denied message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Access Denied");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            // Returning a 500 status with a generic error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Map<String, String>> getAccountBalance(@PathVariable Long accountId, Authentication authentication) {
        try {
            String authenticatedEmail = authentication.getName();
            BigDecimal balance = accountServiceImple.getAccountBalance(accountId, authenticatedEmail);
            Map<String, String> response = new HashMap<>();
            response.put("balance", balance.toString());

            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Access Denied");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Internal Server Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
