package healthcare.entity.dto.resp;

import healthcare.entity.Role;
import healthcare.entity.enums.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AccountUpdateResp {
    private String nickName;
    private String email;
    private String phone;
    private LocalDate date;
}
