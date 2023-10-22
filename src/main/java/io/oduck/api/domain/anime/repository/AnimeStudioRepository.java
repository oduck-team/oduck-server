package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeStudio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeStudioRepository extends JpaRepository<AnimeStudio, Long> {

    @Query("select distinct ast from AnimeStudio ast "
        + "join fetch ast.anime a "
        + "join fetch ast.studio s "
        + "where ast.anime.id = :animeId")
    List<AnimeStudio> findAllFetchByAnimeId(@Param("animeId") Long animeId);
}
