package io.oduck.api.domain.review.dto;

import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.global.common.EntityBased;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
public class ShortReviewResDto {;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ShortReviewRes implements EntityBased {
        private Long animeId;
        private String name;
        private String thumbnail;
        private Integer score;
        private String content;
        private boolean hasSpoiler;
        private boolean hasLike;
        @Builder.Default
        private Long likeCount = 0L;
        private LocalDateTime createdAt;

        //카운트가 없으면 hasLike false
        @Builder
        public static ShortReviewRes of(ShortReviewDsl shortReviewDsl){
            boolean hasLike = shortReviewDsl.getLikeCount() != null;
            Long likeCount = hasLike ? shortReviewDsl.getLikeCount() : 0L ;
            return ShortReviewRes
                       .builder()
                       .animeId(shortReviewDsl.getAnimeId())
                       .name(shortReviewDsl.getName())
                       .thumbnail(shortReviewDsl.getThumbnail())
                       .score(shortReviewDsl.getScore())
                       .content(shortReviewDsl.getContent())
                       .hasSpoiler(shortReviewDsl.isHasSpoiler())
                       .hasLike(hasLike)
                       .likeCount(likeCount)
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
}
