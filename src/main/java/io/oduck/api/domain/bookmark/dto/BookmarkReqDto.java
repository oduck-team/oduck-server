package io.oduck.api.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class BookmarkReqDto {
    @Getter
    @AllArgsConstructor
    public enum Sort {
        CREATED_AT("createdAt"),
        TITLE("title"),
        SCORE("score");

        private final String sort;
    }

}
