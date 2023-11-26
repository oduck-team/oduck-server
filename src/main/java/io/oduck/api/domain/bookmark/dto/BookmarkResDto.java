package io.oduck.api.domain.bookmark.dto;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.global.common.EntityBased;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkResDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkCountRes {
        private Long count;
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BookmarkRes implements EntityBased {
        private Long animeId;
        private String title;
        private String thumbnail;
        private Double avgScore;
        private Integer myScore;
        private LocalDateTime createdAt;

        public static BookmarkRes of(BookmarkDsl bookmarkDsl) {
            Integer myScore = bookmarkDsl.getMyScore();
            Long starRatingScoreTotal = bookmarkDsl.getStarRatingScoreTotal();
            Long starRatingCount = bookmarkDsl.getStarRatingCount();

            BookmarkRes bookmarkRes = BookmarkRes.builder().animeId(bookmarkDsl.getAnimeId())
                .title(bookmarkDsl.getTitle()).thumbnail(bookmarkDsl.getThumbnail())
                .myScore(myScore == null ? -1 : myScore).createdAt(bookmarkDsl.getCreatedAt())
                .build();

            if (starRatingCount <= 0) {
                bookmarkRes.avgScore = 0.0;
            } else {
                bookmarkRes.avgScore = Double.valueOf(starRatingScoreTotal) / starRatingCount;
            }

            return bookmarkRes;
        }

        @Override
        public String bringCursor(String property) {
            switch (property) {
                case "score":
                    return this.myScore.toString() + ", " + this.createdAt.toString();
                case "title":
                    return this.title;
                default:
                    return this.createdAt.toString();
            }
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkedDateTimeRes {
        private String createdAt;
    }
}
