package healthcare.api.service;

import healthcare.entity.Account;
import healthcare.entity.Role;
import healthcare.entity.UserPrin;
import healthcare.entity.dto.account.AccountDto;
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
    private UserDetailsPasswordService userDetailsPasswordService;
    private PasswordEncoder passwordEncoder;


    @Override
    public UserPrin loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<GrantedAuthority> authorities = account.getRole().stream().map( role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new UserPrin( account);
    }

    public Account loadAccByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return account;
    }
    public Account save(AccountDto accountDto) throws Exception {
        Account newAccount = new Account(accountDto);
        long count = repository.countByUsername( accountDto.getUsername());
        if (count >0){
            throw new Exception("account.already.exist");
        }
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setPassword(bcryptEncoder.encode(accountDto.getPassword()));
        Optional<Role> role  = roleRepository.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role.get());
        newAccount.setRole(roles);
        return repository.save(newAccount);
    }


//    protected Authentication createSuccessAuthentication(Object principal,
//                                                         Authentication authentication, UserDetails user) {
//        boolean upgradeEncoding = this.userDetailsPasswordService != null
//                && this.passwordEncoder.upgradeEncoding(user.getPassword());
//        if (upgradeEncoding) {
//            String presentedPassword = authentication.getCredentials().toString();
//            String newPassword = this.passwordEncoder.encode(presentedPassword);
//            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
//        }
//        return super.createSuccessAuthentication(principal, authentication, user);
//    }

    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}
