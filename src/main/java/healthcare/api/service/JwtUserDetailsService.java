package healthcare.api.service;

import healthcare.entity.Account;
import healthcare.entity.Role;
import healthcare.entity.UserPrin;
import healthcare.entity.dto.account.AccountDto;
import healthcare.entity.dto.req.AccReq;
import healthcare.entity.enums.AccountStatus;
import healthcare.repository.AccountRepository;
import healthcare.repository.AuthenRepository;
import healthcare.repository.RoleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtUserDetailsService implements UserDetailsService   {

    @Autowired
    private AuthenRepository repository;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserPrin loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        account.setAge(Period.between(account.getDate(), account.getCurrentDate()).getYears());
        List<GrantedAuthority> authorities = account.getRole().stream().map( role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserPrin( account);
    }

    public Account save(AccReq accReq) throws Exception {
        Account newAccount = new Account(accReq);
        long count = repository.countByUsername( accReq.getUsername());
        if (count >0){
            throw new Exception("account.already.exist");
        }
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setPassword(bcryptEncoder.encode(accReq.getPassword()));
        newAccount.setAge(Period.between(accReq.getDate(), newAccount.getCurrentDate()).getYears());
        Optional<Role> role  = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        role.isPresent();
        roles.add(role.get());
        newAccount.setRole(roles);
        return repository.save(newAccount);
    }
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
