package healthcare.entity.dto.account;


import healthcare.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class AccountDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private String avatar;
    private String phone;
    private String username;
    private String password;
//    private String role;

    private Set<Role> role;
}
