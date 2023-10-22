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

    private List<String> genres;
    private List<String> originalAuthors;
    private List<VoiceActorRes> voiceActors;
    private List<String> studios;
    private long reviewCount;
    private long bookmarkCount;
}
