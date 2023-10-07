package healthcare.api.service;

import healthcare.entity.Account;
import healthcare.entity.Role;
import healthcare.entity.dto.account.AccountDto;
import healthcare.entity.enums.AccountStatus;
import healthcare.repository.AccountRepository;
import healthcare.repository.AuthenRepository;
import healthcare.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService  {

    @Autowired
    private AuthenRepository repository;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
//        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(account.getRole().name()));
        List<GrantedAuthority> authorities = account.getRole().stream().map( role -> new SimpleGrantedAuthority(role.getName() )).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(),
                authorities);
    }

    public Account loadAccByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return account;
    }
    public Account save(AccountDto accountDto) {
        Account newAccount = new Account(accountDto);
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setPassword(bcryptEncoder.encode(accountDto.getPassword()));
        Optional<Role> role  = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role.get());
        newAccount.setRole(roles);
        return repository.save(newAccount);
    }
}
