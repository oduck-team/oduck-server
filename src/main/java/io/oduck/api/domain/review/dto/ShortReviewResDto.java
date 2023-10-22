package io.oduck.api.domain.review.dto;

import io.oduck.api.global.common.EntityBased;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@Builder
public class ShortReviewResDto {
    private Slice<ShortReview> shortReviews;

    @Getter
    @Builder
    public static class ShortReview implements EntityBased {
        private Long reviewId;
        private Long animeId;
        private String content;
        private boolean hasSpoiler;
        private int score;
        private int shortReviewLikeCount;
        private MemberProfile member;

        @Override
        public String bringId(String property) {
            return this.reviewId.toString();
        }
    }

    @Builder
    @Getter
    public static class MemberProfile{
        private String name;
        private String thumbnail;
    }
}
