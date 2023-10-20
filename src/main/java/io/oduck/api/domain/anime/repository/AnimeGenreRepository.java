package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeGenre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeGenreRepository extends JpaRepository<AnimeGenre, Long> {

    @Query("select ag from AnimeGenre ag "
        + "join ag.anime "
        + "join ag.genre "
        + "where ag.anime.id = :animeId")
    List<AnimeGenre> findAllByAnimeId(@Param("animeId") Long animeId);
}
