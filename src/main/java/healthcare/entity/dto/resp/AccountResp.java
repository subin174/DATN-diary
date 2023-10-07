package healthcare.entity.dto.resp;

import healthcare.entity.enums.AccountStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private String username;
    private String password;
    private String role;
    private LocalDateTime createdAt;
    private String status;
}
