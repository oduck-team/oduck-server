package io.oduck.api.domain.inquiry.dto;

import io.oduck.api.domain.inquiry.entity.FeedbackType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerFeedback {
    private Long inquiryId;
    private FeedbackType helpful;

    public static AnswerFeedback from(Long inquiryId, FeedbackType helpful) {
        return new AnswerFeedback(inquiryId, helpful);
    }
}
