package healthcare.repository;

import healthcare.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenRepository extends CrudRepository<Account,Integer> {
    Account findByUsername(String username);
    long countByUsername( String username);
}
