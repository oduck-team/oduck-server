package io.oduck.api.domain.review.dto;

import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDslWithTitle;
import io.oduck.api.global.common.EntityBased;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortReviewResDto {

    ;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ShortReviewRes implements EntityBased {

        private Long reviewId;
        private Long animeId;
        private String name;
        private String thumbnail;
        private Integer score;
        private String content;
        private Boolean isSpoiler;
        @Builder.Default
        private Long likeCount = 0L;
        private LocalDateTime createdAt;

        //카운트가 없으면 hasLike false
        @Builder
        public static ShortReviewRes of(ShortReviewDsl shortReviewDsl) {
            return ShortReviewRes
                .builder()
                .reviewId(shortReviewDsl.getReviewId())
                .animeId(shortReviewDsl.getAnimeId())
                .name(shortReviewDsl.getName())
                .thumbnail(shortReviewDsl.getThumbnail())
                .score(shortReviewDsl.getScore())
                .content(shortReviewDsl.getContent())
                .isSpoiler(shortReviewDsl.getIsSpoiler())
                .likeCount(shortReviewDsl.getLikeCount())
                .createdAt(shortReviewDsl.getCreatedAt())
                .build();
        }

        @Override
        public String bringCursor(String property) {
            return switch (property) {
                case "likeCount" -> this.likeCount.toString() + ", " + this.createdAt.toString();
                case "score" -> this.score.toString() + ", " + this.createdAt.toString();
                default -> this.createdAt.toString();
            };
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ShortReviewResWithTitle implements EntityBased {

        private Long reviewId;
        private Long animeId;
        private String title;
        private String thumbnail;
        private Integer score;
        private String content;
        private Boolean isSpoiler;
        private Long likeCount;
        private LocalDateTime createdAt;

        public static ShortReviewResWithTitle of(ShortReviewDslWithTitle shortReviewDsl) {
            Integer score = shortReviewDsl.getScore();
            return ShortReviewResWithTitle
                .builder()
                .reviewId(shortReviewDsl.getReviewId())
                .animeId(shortReviewDsl.getAnimeId())
                .title(shortReviewDsl.getTitle())
                .thumbnail(shortReviewDsl.getThumbnail())
                .score(score != null ? score : -1)
                .content(shortReviewDsl.getContent())
                .isSpoiler(shortReviewDsl.getIsSpoiler())
                .likeCount(shortReviewDsl.getLikeCount())
                .createdAt(shortReviewDsl.getCreatedAt())
                .build();
        }

        @Override
        public String bringCursor(String property) {
            return switch (property) {
                case "title" -> this.title;
                case "score" -> this.score.toString() + ", " + this.createdAt.toString();
                default -> this.createdAt.toString();
            };
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortReviewCountRes {

        private Long count;
    }
}
