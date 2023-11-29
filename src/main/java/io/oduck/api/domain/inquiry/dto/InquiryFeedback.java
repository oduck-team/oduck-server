package io.oduck.api.domain.inquiry.dto;

import io.oduck.api.domain.inquiry.entity.FeedbackType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryFeedback {
    private Long inquiryId;
    private FeedbackType helpful;

    public static InquiryFeedback from(Long inquiryId, FeedbackType helpful) {
        return new InquiryFeedback(inquiryId, helpful);
    }
}
