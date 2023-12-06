package io.oduck.api.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class ShortReviewReqDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ShortReviewReq{
        private Long animeId;
//        private String name;
        private boolean hasSpoiler;
        @NotBlank
        @Length(min = 10, max = 100,
            message = "최소 10에서 100자 까지 입력 가능합니다.")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public enum Sort{
        //좋아요순(기본), 최신순, 평점 높은순, 평점 낮은순으로 조회 가능
        CREATED_AT("createdAt"),
        LIKE_COUNT("likeCount"),
        SCORE("score");

        private final String sort;
    }

    @Getter
    @AllArgsConstructor
    public enum SortForProfile{
        //제목, 최신순, 평점 높은순
        CREATED_AT("createdAt"),
        TITLE("title"),
        SCORE("score");

        private final String sort;
    }
}