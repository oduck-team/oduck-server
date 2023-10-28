package io.oduck.api.domain.anime.dto;

import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

public class AnimeReq {

    @Getter
    public static class PostReq {
        @NotBlank
        @Length(min = 1, max = 50, message = "글자 수는 0~50을 허용합니다.")
        private String title;

        @NotBlank
        @Length(min = 1, max = 255, message = "글자 수는 0~255를 허용합니다.")
        private String summary;

        @NotNull
        private BroadcastType broadcastType;

        @NotNull(message = "에피소드 수를 입력하세요.")
        @Min(value = 0, message = "음수가 올 수 없습니다.")
        private int episodeCount;

        @NotBlank(message = "섬네일을 입력하세요.")
        private String thumbnail;

        @NotNull(message = "년도를 입력하세요.")
        @Min(value = 1900, message = "1900년대 이상을 입력하세요.")
        private int year;

        @NotNull(message = "분기를 입력하세요.")
        private Quarter quarter;

        @NotNull(message = "심의를 입력하세요.")
        private Rating rating;

        @NotNull(message = "방영 상태를 입력하세요.")
        private Status status;

        private boolean isReleased = false;

        private List<Long> originalAuthorIds;

        private List<Long> studioIds;

        private List<AnimeVoiceActorReq> voiceActors;

        private List<Long> genreIds;

        private Long seriesId;

        public PostReq (String title, String summary, BroadcastType broadcastType,
            int episodeCount, String thumbnail, int year, Quarter quarter, Rating rating, Status status, Boolean isReleased,
            List<Long> originalAuthorIds, List<Long> studioIds, List<AnimeVoiceActorReq> voiceActors,
            List<Long> genreIds, Long seriesId) {

            this.title = title;
            this.summary = summary;
            this.broadcastType = broadcastType;
            this.episodeCount = episodeCount;
            this.thumbnail = thumbnail;
            this.year = year;
            this.quarter = quarter;
            this.rating = rating;
            this.status = status;

            if(isReleased == null) {
                this.isReleased = isReleased;
            }

            if(originalAuthorIds == null){
                this.originalAuthorIds = new ArrayList<>();
            }else {
                this.originalAuthorIds = originalAuthorIds;
            }

            if(studioIds == null){
                this.studioIds = new ArrayList<>();
            }else{
                this.studioIds = studioIds;
            }

            if(voiceActors == null) {
                this.voiceActors = new ArrayList<>();
            }else {
                this.voiceActors = voiceActors;
            }

            if(genreIds == null) {
                this.genreIds = new ArrayList<>();
            }else {
                this.genreIds = genreIds;
            }

            this.seriesId = seriesId;
        }
    }

    @Getter
    public static class PatchAnimeReq{
        @NotBlank
        @Length(min = 1, max = 50, message = "글자 수는 0~50을 허용합니다.")
        private String title;

        @NotBlank
        @Length(min = 1, max = 255, message = "글자 수는 0~255를 허용합니다.")
        private String summary;

        @NotNull
        private BroadcastType broadcastType;

        @NotNull(message = "에피소드 수를 입력하세요.")
        @Min(value = 0, message = "음수가 올 수 없습니다.")
        private int episodeCount;

        @NotBlank(message = "섬네일을 입력하세요.")
        private String thumbnail;

        @NotNull(message = "년도를 입력하세요.")
        @Min(value = 1900, message = "1900년대 이상을 입력하세요.")
        private int year;

        @NotNull(message = "분기를 입력하세요.")
        private Quarter quarter;

        @NotNull(message = "심의를 입력하세요.")
        private Rating rating;

        @NotNull(message = "방영 상태를 입력하세요.")
        private Status status;

        private boolean isReleased = false;

        public PatchAnimeReq(String title, String summary, BroadcastType broadcastType,
            int episodeCount, String thumbnail, int year, Quarter quarter, Rating rating, Status status,
            Boolean isReleased) {
            this.title = title;
            this.summary = summary;
            this.broadcastType = broadcastType;
            this.episodeCount = episodeCount;
            this.thumbnail = thumbnail;
            this.year = year;
            this.quarter = quarter;
            this.rating = rating;
            this.status = status;
            if(isReleased != null){
                this.isReleased = isReleased;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PatchOriginalAuthorIdsReq {
        private List<Long> originalAuthorIds;

        public PatchOriginalAuthorIdsReq(List<Long> originalAuthorIds) {
            if(originalAuthorIds == null){
                this.originalAuthorIds = new ArrayList<>();
            }else{
                this.originalAuthorIds = originalAuthorIds;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PatchStudioIdsReq {
        private List<Long> studioIds;

        public PatchStudioIdsReq(List<Long> studioIds) {
            if(studioIds == null){
                this.studioIds = new ArrayList<>();
            }else{
                this.studioIds = studioIds;
            }
        }

    }

    @Getter
    @NoArgsConstructor
    public static class PatchVoiceActorIdsReq {
        private List<AnimeVoiceActorReq> voiceActors;

        public PatchVoiceActorIdsReq(List<AnimeVoiceActorReq> voiceActors) {
            if(voiceActors == null){
                this.voiceActors = new ArrayList<>();
            }else{
                this.voiceActors = voiceActors;
            }
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PatchGenreIdsReq {
        private List<Long> genreIds;

        public PatchGenreIdsReq(List<Long> genreIds) {
            if(genreIds == null){
                this.genreIds = new ArrayList<>();
            }else{
                this.genreIds = genreIds;
            }
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchSeriesIdReq{
        private Long seriesId;
    }

    @Getter
    @AllArgsConstructor
    public enum Sort {
        LATEST("createdAt"),
        REVIEW_COUNT("reviewCount"),
        SCORE("score");

        private final String sort;
    }

    @Getter
    @ToString
    public static class SearchFilter {
        List<Long> genreIds;
        List<BroadcastType> broadcastTypes;
        List<Status> statuses;
        List<EpisodeCountEnum> episodeCounts;
        List<Integer> years;
        List<Quarter> quarters;

        public SearchFilter(List<Long> genreIds, List<BroadcastType> broadcastTypes, List<Status> statuses, List<EpisodeCountEnum> episodeCounts, List<Integer> years, List<Quarter> quarters) {
            this.genreIds = genreIds;
            this.broadcastTypes = broadcastTypes;
            this.statuses = statuses;
            this.episodeCounts = episodeCounts;
            this.years = years;
            this.quarters = quarters;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum EpisodeCountEnum {
        UNDER_TWELVE(12),
        UNDER_TWENTY_FOUR(24),
        UNDER_FORTY_EIGHT(48),
        UNDER_HUNDRED(100),
        OVER_HUNDRED(100);

        private final int count;
    }
}
