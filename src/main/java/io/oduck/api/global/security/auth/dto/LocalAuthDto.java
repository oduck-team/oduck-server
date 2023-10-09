package io.oduck.api.global.security.auth.dto;

import lombok.Getter;

@Getter
public class LocalAuthDto {
    private String email;
    private String password;
}
