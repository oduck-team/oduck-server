package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeStudio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeStudioRepository extends JpaRepository<AnimeStudio, Long> {

    @Query("select ast from AnimeStudio ast "
        + "join ast.anime "
        + "join ast.studio "
        + "where ast.anime.id = :animeId")
    List<AnimeStudio> findAllByAnimeId(@Param("animeId") Long animeId);
}
