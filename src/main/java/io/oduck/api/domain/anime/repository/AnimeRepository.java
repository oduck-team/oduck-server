package io.oduck.api.domain.anime.repository;

import io.lettuce.core.dynamic.annotation.Param;
import io.oduck.api.domain.anime.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    @Query("select a from Anime a where a.id = :animeId and a.deletedAt = null and a.isReleased = true")
    Optional<Anime> findReleasedAnimeById(@Param("animeId") Long animeId);
}
