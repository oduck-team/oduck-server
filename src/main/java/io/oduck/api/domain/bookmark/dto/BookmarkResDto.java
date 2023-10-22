package io.oduck.api.domain.bookmark.dto;

import io.oduck.api.domain.bookmark.dto.BookmarkDslDto.BookmarkDsl;
import io.oduck.api.global.common.EntityBased;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkResDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkRes implements EntityBased {
        private Long animeId;
        private String title;
        private String thumbnail;
        private Integer myScore;
        private LocalDateTime createdAt;

        public static BookmarkRes of(BookmarkDsl bookmarkDsl) {
            Integer myScore = bookmarkDsl.getMyScore();

            return BookmarkRes.builder().animeId(bookmarkDsl.getAnimeId())
                    .title(bookmarkDsl.getTitle()).thumbnail(bookmarkDsl.getThumbnail())
                    .myScore(myScore == null ? -1 : myScore).createdAt(bookmarkDsl.getCreatedAt())
                    .build();
        }

        @Override
        public String bringId(String property) {
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
}
