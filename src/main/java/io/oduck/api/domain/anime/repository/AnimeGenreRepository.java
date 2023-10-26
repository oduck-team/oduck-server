package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeGenre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeGenreRepository extends JpaRepository<AnimeGenre, Long> {

    @Query("select distinct ag from AnimeGenre ag "
        + "join fetch ag.anime a "
        + "join fetch ag.genre g "
        + "where ag.anime.id = :animeId")
    List<AnimeGenre> findAllFetchByAnimeId(@Param("animeId") Long animeId);
}
