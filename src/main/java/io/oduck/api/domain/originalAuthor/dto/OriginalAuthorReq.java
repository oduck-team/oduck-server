package io.oduck.api.domain.originalAuthor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class OriginalAuthorReq {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostReq {
        @NotBlank
        @Length(min = 1, max = 50,
        message = "글자 수는 0~50을 허용합니다.")
        private String name;
    }
}
