package io.oduck.api.domain.bookmark.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkDslDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkDsl {
        private Long animeId;
        private String title;
        private String thumbnail;
        private Long starRatingScoreTotal;
        private Long starRatingCount;
        private Integer myScore;
        private LocalDateTime createdAt;
    }
}
