package io.oduck.api.domain.anime.repository;

import io.lettuce.core.dynamic.annotation.Param;
import io.oduck.api.domain.anime.entity.Anime;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long>, AnimeRepositoryCustom {

    @Query("select a from Anime a where a.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Optional<Anime> findByIdForUpdate(@Param("id")Long id);
  
    @Query("select a from Anime a where a.id = :id and a.deletedAt = null and a.isReleased = true")
    Optional<Anime> findReleasedAnimeById(@Param("id") Long id);
  
}
