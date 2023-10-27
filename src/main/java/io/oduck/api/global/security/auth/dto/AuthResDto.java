package io.oduck.api.global.security.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthResDto {
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    public static class Status {
        private Long memberId;
        private String role;
        private String name;
        private String description;
        private String thumbnail;
        private Long point;
    }
}
