package healthcare.repository;

import healthcare.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>, JpaSpecificationExecutor<Account> {
    Account findByPhone(String phone);

    @Query(value = " SELECT COUNT(*) AS count " +
            "FROM `diary-app`.account a " +
            "WHERE a.created_at BETWEEN ?1 AND ?2" , nativeQuery = true)
    List<Object> getCountAccountByYear(String startDate, String endDate);

    @Query(value = " SELECT COUNT(*) AS count " +
            "FROM `diary-app`.account a ", nativeQuery = true)
    List<Object> getCountQuantityAccount();
    @Query(value = " SELECT AVG(age) AS average_age " +
            "FROM `diary-app`.account ", nativeQuery = true)
    Optional<Object> getAverageAge();

}
