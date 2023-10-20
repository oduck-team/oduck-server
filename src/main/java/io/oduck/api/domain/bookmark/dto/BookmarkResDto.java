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

            return BookmarkRes.builder()
                .animeId(bookmarkDsl.getAnimeId())
                .title(bookmarkDsl.getTitle())
                .thumbnail(bookmarkDsl.getThumbnail())
                .myScore(bookmarkDsl.getMyScore() == null ? -1 : bookmarkDsl.getMyScore())
                .createdAt(bookmarkDsl.getCreatedAt())
                .build();
        }

        @Override
        public Long bringId() {
            return this.animeId;
        }
    }
}
