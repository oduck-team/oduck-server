package io.oduck.api.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class MemberReqDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReq {

        @NotBlank
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_=+-])(?=.*[0-9]).{8,20}$",
                message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 포함한 8~20자리여야 합니다.")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class PatchReq {

        @NotBlank
        @Pattern(regexp = "^[0-9A-Za-z가-힣_]{2,10}$",
                message = "이름은 2~10자리의 한글, 영문, 숫자여야 합니다.")
        private String name;

        @Length(min = 0, max = 100,
                message = "자기 소개는 100자 이내여야 합니다.")
        private String description;

        @Builder
        public PatchReq(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
