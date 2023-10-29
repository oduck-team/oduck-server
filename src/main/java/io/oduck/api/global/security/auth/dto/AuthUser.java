package io.oduck.api.global.security.auth.dto;

import io.oduck.api.domain.member.entity.LoginType;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthUser implements Serializable {
    private Long id;
    private String role;
    private LoginType loginType;

    public AuthUser(Long id, LoginType loginType) {
        this.id = id;
        this.loginType = loginType;
    }
}