package io.oduck.api.global.security.auth.dto;

import io.oduck.api.domain.member.entity.LoginType;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthUser implements Serializable {
    private Long id;
    private LoginType loginType;

    @Builder
    public AuthUser(Long id, LoginType loginType) {
        this.id = id;
        this.loginType = loginType;
    }
}