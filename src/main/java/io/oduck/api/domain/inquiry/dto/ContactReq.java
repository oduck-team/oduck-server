package io.oduck.api.domain.inquiry.dto;

import io.oduck.api.domain.inquiry.entity.InquiryType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class ContactReq {
    @Getter
    @AllArgsConstructor
    public static class PostReq {
        @NotBlank
        @Length(min = 1, max = 50,
            message = "글자 수는 1~50을 허용합니다.")
        private InquiryType type;

        @NotBlank
        @Length(min = 1, max = 50,
            message = "글자 수는 1~50을 허용합니다.")
        private String title;

        @NotBlank
        @Length(min = 1, max = 1000,
            message = "글자 수는 1~1000를 허용합니다.")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class AnswerReq {
        @NotBlank
        @Length(min = 1, max = 1000,
            message = "글자 수는 1~1000를 허용합니다.")
        private String content;
    }


    @Getter
    @AllArgsConstructor
    public static class AnswerUpdateReq {
        @NotBlank
        @Length(min = 1, max = 1000,
            message = "글자 수는 1~1000를 허용합니다.")
        private String content;
    }
}
