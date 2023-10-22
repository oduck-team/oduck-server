package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimeOriginalAuthorRepository extends JpaRepository<AnimeOriginalAuthor, Long> {

    @Query("select distinct aoa from AnimeOriginalAuthor aoa "
        + "join fetch aoa.anime a "
        + "join fetch aoa.originalAuthor o "
        + "where aoa.anime.id = :animeId")
    List<AnimeOriginalAuthor> findAllFetchByAnimeId(@Param("animeId") Long animeId);
}
