package io.oduck.api.global.security.auth.dto;

import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Role;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class CustomUserDetails extends User {
    private Long id;
    private LoginType loginType;
    public CustomUserDetails(Long id, String username, String password, Role role) {
        super(username, password, Collections.singletonList(new SimpleGrantedAuthority(
            role.toString())));
        this.id = id;
        this.loginType = LoginType.LOCAL;
    }

    // 아래에서부터는 사용하지 않을 것이므로 전부 true 반환
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
