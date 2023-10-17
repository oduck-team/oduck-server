package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime,Long> {

}
