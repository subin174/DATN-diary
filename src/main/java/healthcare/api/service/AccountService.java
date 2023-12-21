package healthcare.api.service;

import healthcare.api.data.ConditionBase;
import healthcare.api.data.FilterReq;
import healthcare.api.data.OperatorBase;
import healthcare.api.data.RequestParams;
import healthcare.entity.Account;
import healthcare.entity.UserPrin;
import healthcare.entity.dto.account.AccountDto;
import healthcare.entity.dto.resp.AccountResp;
import healthcare.entity.enums.Role;
import healthcare.exception.InvalidOldPasswordException;
import healthcare.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountService extends BaseService<Account> {
    @Override
    protected AccountRepository getRepository() {
        return repository;
    }

    @Override
    protected Class getType() {
        return Account.class;
    }
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    final AccountRepository repository;
    final FileService fileService;
    public UserPrin checkUserPermission(String role) {
        UserPrin account = getCurrentUser();
        if (account.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role))){
            return account;
        }
        throw new RuntimeException("permission.denied");
    }
    public UserPrin getCurrentUser() {
        try {
            UserPrin user = (UserPrin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (null == user) {
                throw new NoSuchElementException("error.user.login");
            }
            return user;
        } catch (Exception e) {
            throw new NoSuchElementException("error.user.login");
        }
    }
    public String getUsername()
    {
        UserPrincipal principal
                = (UserPrincipal)SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getName();
    }

    public List<?> getList(RequestParams params) throws Exception {
        checkUserPermission(Role.ADMIN.name());
        Specification<Account> specification = this.buildSpecification(params.getFilter());
        List<Account> accounts =  this.getAll(specification);
        return accounts.stream().map(account -> this.entityToResp(account,AccountResp.class))
                .collect(Collectors.toList());
    }
    public List<Account> getUser(){
        return repository.findAll();
    }

    public Page<?> getPage(RequestParams params) throws Exception {
        checkUserPermission(Role.ADMIN.name());
        Specification<Account> specification = this.buildSpecification(params.getFilter());
        Page<Account> accounts =  this.getPaginated(specification,params.getPageable());
        return accounts.map(account -> this.entityToResp(account,AccountResp.class));
    }

    public Account update(AccountResp accountResp) throws Exception {
        UserPrin userPrin = getCurrentUser();
        Account account = this.getById(userPrin.getId());
        account.updateAccount(accountResp);

        return this.entityToResp(repository.save(account),Account.class);
    }
    public Account setAvatar(MultipartFile file) throws Exception {
        UserPrin userPrin = getCurrentUser();
        Account account = this.getById(userPrin.getId());
        String img = fileService.uploadImageToImgur(file);
        account.setAvatar(img);
        log.info(img);
        return this.entityToResp(repository.save(account),Account.class);
    }

    public AccountResp updatePass(String oldPassword, String password)throws Exception{
        UserPrin userPrin = getCurrentUser();
        Account account = this.getById(userPrin.getId());
        if (!checkIfValidOldPassword(account, oldPassword)) {
            throw new InvalidOldPasswordException();
        }
        account.setPassword(bcryptEncoder.encode(password));
        account.setPass(password);
        return this.entityToResp(repository.save(account),AccountResp.class);
    }
    public boolean checkIfValidOldPassword(final Account account, final String oldPassword) {
        return bcryptEncoder.matches(oldPassword, account.getPassword());
    }

    public AccountDto findById(Long id)throws Exception{
        Account account = this.getById(id);
        return this.entityToResp(account,AccountDto.class);
    }
    public AccountResp findInfoById(Long id)throws Exception{
        getCurrentUser();
        Account account = this.getById(id);
        return this.entityToResp(account,AccountResp.class);
    }
    public  AccountResp getInfoByUser(){
        UserPrin user = this.getCurrentUser();
        Account account = repository.findById(user.getId()).get();
        return this.entityToResp(account, AccountResp.class);
    }

    public AccountResp findByPhone(String phone){
        Account account = repository.findByPhone(phone);
        return this.entityToResp(account,AccountResp.class);
    }

    public AccountDto approve(Long id) {
        Account account = this.getById(id);
//        account.setRole(Role.ADMIN);
        return this.entityToResp(this.save(account), AccountDto.class);
    }
    public void delete(Long id){
        checkUserPermission(Role.ADMIN.name());
        this.deleteById(id);
    }
    public void deleteAccountAndRoles(Long Id) {
        Account account = entityManager.find(Account.class,Id);
        if (account != null) {
            account.getRole().clear();
            entityManager.remove(account);
        }
    }
    public FilterReq getFilterByUser(Long id) {
        return FilterReq.builder()
                .field("createdBy")
                .values(Collections.singletonList(id.toString()))
                .operator(OperatorBase.EQUALS)
                .condition(ConditionBase.AND)
                .build();
    }
}
