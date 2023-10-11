package healthcare.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPrin implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private  Collection<? extends GrantedAuthority> authorities;

    private Map<String,Object> attributes;

    public UserPrin(Account account){
        this.id=account.getId();
        this.attributes = new HashMap<>();
        this.attributes.put("avatar",account.getAvatar());
        this.attributes.put("nickname",account.getNickName());
        this.password = account.getPassword();
        this.username = account.getUsername();
        this.authorities = account.getRole().stream().map( role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
