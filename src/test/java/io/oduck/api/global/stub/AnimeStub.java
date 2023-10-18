package io.oduck.api.global.stub;

import io.oduck.api.domain.anime.entity.Anime;
import java.util.ArrayList;
import java.util.List;

public class AnimeStub {
    List<Anime> animes = new ArrayList<>();

    public AnimeStub() {
        Anime anime1 = Anime.builder()
            .title("anime1")
            .thumbnail("http://thumbnail.com")
            .summary("summary")
            .episodeCount(12)
            .isReleased(true)
            .build();

        Anime anime2 = Anime.builder()
            .title("anime2")
            .thumbnail("http://thumbnail.com")
            .summary("summary")
            .episodeCount(12)
            .isReleased(true)
            .build();

        Anime anime3 = Anime.builder()
            .title("anime3")
            .thumbnail("http://thumbnail.com")
            .summary("summary")
            .episodeCount(12)
            .isReleased(true)
            .build();

        animes.addAll(List.of(anime1, anime2, anime3));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
