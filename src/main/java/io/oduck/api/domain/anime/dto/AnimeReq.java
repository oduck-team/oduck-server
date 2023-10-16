package io.oduck.api.domain.anime.dto;

import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class AnimeReq {

    @Getter
    public static class PostReq {
        @NotBlank
        @Length(min = 1, max = 50,
            message = "글자 수는 0~50을 허용합니다.")
        private String title;

        @NotBlank
        @Length(min = 1, max = 255,
            message = "글자 수는 0~255를 허용합니다.")
        private String summary;

        private BroadcastType broadcastType;

        @Min(value = 0,
            message = "음수가 올 수 없습니다.")
        private int episodeCount;

        @NotBlank(
            message = "섬네일을 입력하세요.")
        private String thumbnail;

        @Min(value = 1900,
            message = "1900년대 이상을 입력하세요.")
        private int year;

        private Quarter quarter;

        private Rating rating;

        private Status status;

        private List<Long> originalAuthorIds;

        private List<Long> studioIds;

        private List<Long> voiceActorIds;

        private List<Long> genreIds;

        private Long seriesId;

        public PostReq (String title, String summary, BroadcastType broadcastType,
            int episodeCount, String thumbnail, int year, Quarter quarter, Rating rating, Status status,
            List<Long> originalAuthorIds, List<Long> studioIds, List<Long> voiceActorIds,
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

            if(voiceActorIds == null) {
                this.voiceActorIds = new ArrayList<>();
            }else {
                this.voiceActorIds = voiceActorIds;
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
    @AllArgsConstructor
    public static class PatchAnimeReq{
        @NotBlank
        @Length(min = 1, max = 50,
            message = "글자 수는 0~50을 허용합니다.")
        private String title;

        @NotBlank
        @Length(min = 1, max = 255,
            message = "글자 수는 0~255를 허용합니다.")
        private String summary;

        private BroadcastType broadcastType;

        @Min(value = 0,
            message = "음수가 올 수 없습니다.")
        private int episodeCount;

        @NotBlank(
            message = "섬네일을 입력하세요.")
        private String thumbnail;

        @Min(value = 1900,
            message = "1900년대 이상을 입력하세요.")
        private int year;

        private Quarter quarter;

        private Rating rating;

        private Status status;
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
        private List<Long> voiceActorIds;

        public PatchVoiceActorIdsReq(List<Long> voiceActorIds) {
            if(voiceActorIds == null){
                this.voiceActorIds = new ArrayList<>();
            }else{
                this.voiceActorIds = voiceActorIds;
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
}
