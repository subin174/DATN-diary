package healthcare.entity.dto.resp;

import healthcare.entity.Role;
import healthcare.entity.enums.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AccountResp {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private String avatar;
    private String phone;
    private LocalDate date;
    private Set<Role> role;
    private String createdAt;
    private AccountStatus status;
}
