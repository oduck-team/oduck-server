package io.oduck.api.domain.weekly.repository;

import io.oduck.api.domain.weekly.entity.WeeklyAnime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyAnimeRepository extends JpaRepository<WeeklyAnime,Long>, WeeklyAnimeRepositoryCustom {

}
