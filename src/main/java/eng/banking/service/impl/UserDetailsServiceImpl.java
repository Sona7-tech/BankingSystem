package eng.banking.service.impl;


import eng.banking.entity.Authority;
import eng.banking.entity.Customer;
import eng.banking.repository.AuthorityRepository;
import eng.banking.repository.CustomerRepository;
import eng.banking.request.RegisterRequest;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  UserDetailsServiceImpl implements UserDetailsService{

    private final CustomerRepository customerRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User details not found for the user: " + username));
        List<GrantedAuthority> roles = customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new User(customer.getEmail(), customer.getPwd(), roles);
    }

    public String registerUser(RegisterRequest registerRequest) {
        Customer customer = modelMapper.map(registerRequest, Customer.class);
        customer.setPwd(passwordEncoder.encode(registerRequest.getPwd()));
        Authority defaultRole = authorityRepository.findByName("ROLE_CUSTOMER");
        customer.getRoles().add(defaultRole);
        customerRepository.save(customer);
        return "User registered successfully with ID: " + customer.getId();
    }
}
