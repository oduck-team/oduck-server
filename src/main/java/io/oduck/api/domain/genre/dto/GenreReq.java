package io.oduck.api.domain.genre.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class GenreReq {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostReq {
        @NotBlank
        @Length(min = 1, max = 15,
                message = "글자 수는 1~15을 허용합니다.")
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchReq {
        @NotBlank
        @Length(min = 1, max = 15,
            message = "글자 수는 1~15을 허용합니다.")
        private String name;
    }
}
