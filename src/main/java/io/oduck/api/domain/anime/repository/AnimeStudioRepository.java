package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.entity.AnimeStudio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeStudioRepository extends JpaRepository<AnimeStudio, Long> {

}
