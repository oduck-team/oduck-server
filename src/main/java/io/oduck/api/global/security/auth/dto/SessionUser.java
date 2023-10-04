package io.oduck.api.global.security.auth.dto;

import io.oduck.api.domain.member.entity.LoginType;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
    private Long id;
    private LoginType loginType;

    public SessionUser(Long id, LoginType loginType) {
        this.id = id;
        this.loginType = loginType;
    }
}