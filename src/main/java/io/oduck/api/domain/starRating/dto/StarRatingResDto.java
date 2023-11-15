package io.oduck.api.domain.starRating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StarRatingResDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatedRes {
        private int score;
        private String createdAt;
    }

}
