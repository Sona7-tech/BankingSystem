package eng.banking.controller;

import eng.banking.constants.ApplicationConstants;
import eng.banking.entity.Customer;
import eng.banking.repository.CustomerRepository;
import eng.banking.request.LoginRequest;
import eng.banking.request.LoginResponse;
import eng.banking.request.RegisterRequest;
import eng.banking.response.CustomerResponse;
import eng.banking.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    private final Environment env;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        String result = userDetailsServiceImpl.registerUser(registerRequest);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public CustomerResponse getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.map(customer -> modelMapper.map(customer, CustomerResponse.class)).orElse(null);
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponse> apiLogin(@RequestBody LoginRequest loginRequest) {
        String jwt = "";
        try {
            Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
            Authentication authenticationResponse = authenticationManager.authenticate(authentication);

            if (authenticationResponse != null && authenticationResponse.isAuthenticated()) {
                if (env != null) {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                    jwt = Jwts.builder()
                            .setIssuer("Bank System")
                            .setSubject("JWT Token")
                            .claim("username", authenticationResponse.getName())
                            .claim("roles", authenticationResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                            .setIssuedAt(new java.util.Date())
                            .setExpiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
                            .signWith(secretKey)
                            .compact();
                    System.out.println("Generated JWT: '" + jwt + "'");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("An unexpected error occurred", null));
        }
        return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER, jwt).body(new LoginResponse(HttpStatus.OK.getReasonPhrase(), jwt));
    }

}
