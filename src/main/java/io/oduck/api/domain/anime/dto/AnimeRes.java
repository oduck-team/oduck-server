package io.oduck.api.domain.anime.dto;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import io.oduck.api.global.common.EntityBased;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnimeRes {

    @Getter
    public static class DetailResult {
        private Long id;
        private String title;
        private String thumbnail;
        private BroadcastType broadcastType;
        private int year;
        private Quarter quarter;
        private String summary;
        private int episodeCount;
        private Rating rating;
        private Status status;

        private List<String> originalAuthors;
        private List<AnimeVoiceActorRes> voiceActors;
        private List<String> genres;
        private List<String> studios;
        private long reviewCount;
        private long bookmarkCount;

        public DetailResult(Anime anime, List<AnimeOriginalAuthor> animeOriginalAuthors,
                        List<AnimeVoiceActor> animeVoiceActors, List<AnimeStudio> animeStudios,
                        List<AnimeGenre> animeGenres) {
            id = anime.getId();
            title = anime.getTitle();
            thumbnail = anime.getThumbnail();
            broadcastType = anime.getBroadcastType();
            year = anime.getYear();
            quarter = anime.getQuarter();
            summary = anime.getSummary();
            episodeCount = anime.getEpisodeCount();
            rating = anime.getRating();
            status = anime.getStatus();
            originalAuthors = animeOriginalAuthors.stream()
                    .map(aoa -> aoa.getOriginalAuthor().getName())
                    .collect(Collectors.toList());
            voiceActors = animeVoiceActors.stream()
                    .map(ava -> new AnimeVoiceActorRes(ava.getVoiceActor().getName(), ava.getPart()))
                    .collect(Collectors.toList());
            genres = animeGenres.stream()
                    .map(ag -> ag.getGenre().getName())
                    .collect(Collectors.toList());
            studios = animeStudios.stream()
                    .map(as -> as.getStudio().getName())
                    .collect(Collectors.toList());
            reviewCount = anime.getReviewCount();
            bookmarkCount = anime.getBookmarkCount();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SearchResult implements EntityBased {
        private Long id;
        private String title;
        private String thumbnail;
        private Double starScoreAvg;

        public SearchResult(Long id, String title, String thumbnail, Long starRatingScoreTotal, Long starRatingCount) {
            this.id = id;
            this.title = title;
            this.thumbnail = thumbnail;

            // starRatingCount 0인 상태에서  starRatingScoreTotal와 나누려고 하면 java.lang.ArithmeticException: / by zero 예외 터짐
            if (starRatingCount <= 0) {
                this.starScoreAvg = 0.0;
            } else {
                this.starScoreAvg = Double.valueOf(starRatingScoreTotal) / starRatingCount;
            }
        }

        @Override
        public String bringCursor(String property) {
            return id.toString();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class StarRatingAvg {
        private Double starRatingAvg;

        public StarRatingAvg(Long starRatingScoreTotal, Long starRatingCount) {
            this.starRatingAvg = calculateAvg(starRatingScoreTotal, starRatingCount);
        }

        private double calculateAvg(Long starRatingScoreTotal, Long starRatingCount) {
            if(starRatingCount <= 0) {
                return 0;
            }
            return starRatingScoreTotal / starRatingCount;
        }
    }

}
