package healthcare.repository;

import healthcare.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>, JpaSpecificationExecutor<Account> {
    Account findByPhone(String phone);
}
