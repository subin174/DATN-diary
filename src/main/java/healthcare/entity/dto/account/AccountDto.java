package healthcare.entity.dto.account;
import healthcare.entity.Account;
import healthcare.entity.Role;
import healthcare.entity.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private String avatar;
    private String phone;
    private String username;
    private String password;
    private LocalDate date;
//    private String role;
    private Set<Role> role;
    private AccountStatus status;

    public AccountDto(Account account){
        BeanUtils.copyProperties(account, this);
    }
}
