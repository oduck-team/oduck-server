package io.oduck.api.domain.admin.dto;

import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Status;
import java.time.LocalDateTime;
import lombok.Getter;

public class AdminRes {

    @Getter
    public static class SearchResult {
        private Long id;
        private String title;
        private String thumbnail;
        private int year;
        private Quarter quarter;
        private Boolean isReleased;
        private Status status;
        private LocalDateTime createdAt;
        private Long seriesId;
        private String seriesTitle;
        private Long bookmarkCount;
        private Long starRatingScoreTotal;
        private Long starRatingCount;
        private double starRatingAvg;
        private Long reviewCount;
        private Long viewCount;

        public SearchResult(Long id, String title, String thumbnail, int year, Quarter quarter,
            boolean isReleased, Status status, LocalDateTime createdAt, Long seriesId, String seriesTitle,
            Long bookmarkCount, Long starRatingScoreTotal, Long starRatingCount, Long reviewCount, Long viewCount) {
            this.id = id;
            this.title = title;
            this.thumbnail = thumbnail;
            this.year = year;
            this.quarter = quarter;
            this.isReleased = isReleased;
            this.status = status;
            this.createdAt = createdAt;
            this.seriesId = seriesId;
            this.seriesTitle = seriesTitle;
            this.bookmarkCount = bookmarkCount;
            this.starRatingScoreTotal = starRatingScoreTotal;
            this.starRatingCount = starRatingCount;
            this.starRatingAvg = calculateAvg(starRatingScoreTotal, starRatingCount);
            this.reviewCount = reviewCount;
            this.viewCount = viewCount;
        }

        private double calculateAvg(Long starRatingScoreTotal, Long starRatingCount) {
            if(starRatingCount <= 0) {
                return 0;
            }
            return starRatingScoreTotal / starRatingCount;
        }
    }
}
