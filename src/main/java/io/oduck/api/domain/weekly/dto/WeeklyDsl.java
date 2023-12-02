package io.oduck.api.domain.weekly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WeeklyDsl {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyAnimeDsl {
        private Long animeId;
        private String title;
        private String thumbnail;
        private Long scoreTotal;
        private Long scoreCount;
        private Double rankScore;
    }
}