package io.oduck.api.domain.studio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class StudioReq {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostReq {
        @NotBlank
        @Length(min = 1, max = 50,
            message = "글자 수는 1~50을 허용합니다.")
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchReq {
      @NotBlank
      @Length(min = 1, max = 50,
          message = "글자 수는 1~50을 허용합니다.")
      private String name;
    }
}
