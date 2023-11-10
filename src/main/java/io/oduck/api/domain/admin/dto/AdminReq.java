package io.oduck.api.domain.admin.dto;

import io.oduck.api.domain.anime.entity.Status;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AdminReq {

    @Getter
    @AllArgsConstructor
    public static class SearchFilter {
        private List<Integer> years;
        private List<Status> statuses;
        private Boolean isReleased;
    }

    @Getter
    @AllArgsConstructor
    public enum QueryType {
        TITLE("title"),
        SERIES("series"),
        ID("id");

        private final String type;
    }

    @Getter
    @AllArgsConstructor
    public enum Sort {
        LATEST("createdAt"),
        SERIES("series"),
        TITLE("title"),
        BOOKMARK_COUNT("bookmarkCount"),
        SCORE_TOTAL("starRatingScoreTotal"),
        SCORE_COUNT("starRatingCount"),
        REVIEW_COUNT("reviewCount"),
        VIEW_COUNT("viewCount");

        private final String sort;
    }
}