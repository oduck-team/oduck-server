package io.oduck.api.domain.review.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortReviewResDto {
    private List<ShortReview> shortReview;

    @Getter
    @Builder
    public static class ShortReview{
        private Long animeId;
        private String content;
        private boolean hasSpoiler;
        private int score;
        private int shortReviewLikeCount;
        private MemberProfile member;
    }

    @Builder
    @Getter
    public static class MemberProfile{
        private String name;
        private String thumbnail;
    }
}
