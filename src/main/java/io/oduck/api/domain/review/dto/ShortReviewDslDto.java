package io.oduck.api.domain.review.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortReviewDslDto {

    @Getter
    @NoArgsConstructor
    public static class ShortReviewDsl{
        private Long reviewId;
        private Long animeId;
        private String name;
        private String thumbnail;
        private Integer score;
        private String content;
        private Boolean isSpoiler;
        private Long likeCount;
        private LocalDateTime createdAt;

        @Builder
        public ShortReviewDsl(Long reviewId, Long animeId, String name, String thumbnail, Integer score, String content,
            Boolean isSpoiler, Long likeCount, LocalDateTime createdAt) {
            this.reviewId = reviewId;
            this.animeId = animeId;
            this.name = name;
            this.thumbnail = thumbnail;
            this.score = score;
            this.content = content;
            this.isSpoiler = isSpoiler;
            this.likeCount = likeCount;
            this.createdAt = createdAt;
        }
    }
}