package healthcare.api.service;

import healthcare.api.data.RequestParams;
import healthcare.entity.Account;
import healthcare.entity.dto.account.AccountDto;
import healthcare.entity.dto.resp.AccountResp;
import healthcare.entity.enums.Role;
import healthcare.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService extends BaseService<Account> {
    @Override
    protected AccountRepository getRepository() {
        return repository;
    }

    @Override
    protected Class getType() {
        return Account.class;
    }
    final AccountRepository repository;
    public UserDetails checkUserPermission(String role) {
        UserDetails account = getCurrentUser();
        if (account.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role))){
            return account;
        }
        throw new RuntimeException("permission.denied");
    }
    public UserDetails getCurrentUser() {
        try {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null == user) {
                throw new NoSuchElementException("error.user.login");
            }
            return user;
        } catch (Exception e) {
            throw new NoSuchElementException("error.user.login");
        }
    }
    public List<?> getList(RequestParams params) throws Exception {
        checkUserPermission(Role.ADMIN.name());
        Specification<Account> specification = this.buildSpecification(params.getFilter());
        List<Account> accounts =  this.getAll(specification);
        return accounts.stream().map(account -> this.entityToResp(account,AccountDto.class))
                .collect(Collectors.toList());
    }

    public Page<?> getPage(RequestParams params) throws Exception {
        checkUserPermission(Role.ADMIN.name());
        Specification<Account> specification = this.buildSpecification(params.getFilter());
        Page<Account> accounts =  this.getPaginated(specification,params.getPageable());
        return accounts.map(account -> this.entityToResp(account,AccountDto.class));
    }

    public AccountDto findById(Long id){
        Account account = this.getById(id);
        return this.entityToResp(account,AccountDto.class);
    }
    public AccountDto approve(Long id) {
        Account account = this.getById(id);
//        account.setRole(Role.ADMIN);
        return this.entityToResp(this.save(account), AccountDto.class);
    }


}
