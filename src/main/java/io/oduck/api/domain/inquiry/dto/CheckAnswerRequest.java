package io.oduck.api.domain.inquiry.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckAnswerRequest {
    private Long inquiryId;

    public static CheckAnswerRequest from(Long inquiryId) {
        return new CheckAnswerRequest(inquiryId);
    }
}
