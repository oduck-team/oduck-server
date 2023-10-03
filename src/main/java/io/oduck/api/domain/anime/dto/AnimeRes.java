package io.oduck.api.domain.anime.dto;

import io.oduck.api.domain.anime.entity.BroadcastType;
import io.oduck.api.domain.anime.entity.Quarter;
import io.oduck.api.domain.anime.entity.Rating;
import io.oduck.api.domain.anime.entity.Status;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnimeRes {
    private Anime anime;
    private Score score;

    @Getter
    @Builder
    public static class Anime {
        private Long animeId;
        private String title;
        private String thumbnail;
        private Broadcast broadcast;
        private String summary;
        private int episodeCount;
        private Rating rating;
        private Status status;

        private List<String> genres;
        private List<String> originalAuthors;
        private List<String> voiceActors;
        private List<String> studios;
        private long reviewCount;
        private long bookmarkCount;
    }

    @Getter
    @Builder
    public static class Broadcast {
        private BroadcastType broadcastType;
        private int year;
        private Quarter quarter;
    }

    @Getter
    @Builder
    public static class Score {
        private Double starRatingScoreAverage;
        private int selectedDrawingCount;
        private int selectedStoryCount;
        private int selectedMusicCount;
        private int selectedCharacterCount;
        private int selectedVoiceActorCount;
    }
}
