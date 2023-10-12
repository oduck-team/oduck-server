package io.oduck.api.global.security.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LocalAuthDto {
    private String email;
    private String password;

    @Builder
    public LocalAuthDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
