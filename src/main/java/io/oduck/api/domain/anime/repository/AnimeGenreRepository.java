package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeGenreRepository extends JpaRepository<AnimeGenre, Long> {

}
