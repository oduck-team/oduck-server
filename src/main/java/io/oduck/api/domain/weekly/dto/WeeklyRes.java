package io.oduck.api.domain.weekly.dto;

import io.oduck.api.domain.weekly.dto.WeeklyDsl.WeeklyAnimeDsl;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WeeklyRes {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyAnimeRes {
        private Long animeId;
        private String title;
        private String thumbnail;
        private List<String> genres;
        private Double avgScore;
        private Double rankScore;

        public static WeeklyAnimeRes of(WeeklyAnimeDsl weeklyAnimeDsl) {

            Double avgScore = weeklyAnimeDsl.getScoreCount() == 0 ? 0 : Double.valueOf(weeklyAnimeDsl.getScoreTotal()) / weeklyAnimeDsl.getScoreCount();
            return WeeklyAnimeRes.builder()
                .animeId(weeklyAnimeDsl.getAnimeId())
                .title(weeklyAnimeDsl.getTitle())
                .thumbnail(weeklyAnimeDsl.getThumbnail())
                .avgScore(avgScore)
                .rankScore(weeklyAnimeDsl.getRankScore())
                .build();
        }

        public WeeklyAnimeRes withGenres(List<String> genres) {
            this.genres = genres;
            return this;
        }
    }
}
