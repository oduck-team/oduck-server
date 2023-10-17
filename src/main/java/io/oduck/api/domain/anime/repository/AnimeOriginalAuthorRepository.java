package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeOriginalAuthorRepository extends JpaRepository<AnimeOriginalAuthor, Long> {

    @Query("select aoa from AnimeOriginalAuthor aoa "
        + "join fetch aoa.anime "
        + "join fetch aoa.originalAuthor "
        + "where aoa.anime.id = :animeId")
    List<AnimeOriginalAuthor> findAllByAnimeId(@Param("animeId") Long animeId);
}
