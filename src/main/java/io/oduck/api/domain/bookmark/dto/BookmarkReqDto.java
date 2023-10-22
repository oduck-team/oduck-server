package io.oduck.api.domain.bookmark.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class BookmarkReqDto {
    @Getter
    public static class CreateReq {
        @Positive
        private Long animeId;
    }
    @Getter
    @AllArgsConstructor
    public enum Sort {
        CREATED_AT("createdAt"),
        TITLE("title"),
        SCORE("score");

        private final String sort;
    }

}
