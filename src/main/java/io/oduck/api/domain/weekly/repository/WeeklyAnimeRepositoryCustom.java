package io.oduck.api.domain.weekly.repository;

import io.oduck.api.domain.weekly.dto.WeeklyDsl;
import io.oduck.api.domain.weekly.dto.WeeklyDsl.WeeklyAnimeDsl;
import java.util.List;

public interface WeeklyAnimeRepositoryCustom {
    List<WeeklyAnimeDsl> getWeeklyAnime();
}
