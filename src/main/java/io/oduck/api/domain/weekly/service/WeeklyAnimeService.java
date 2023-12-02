package io.oduck.api.domain.weekly.service;

import io.oduck.api.domain.weekly.dto.WeeklyRes.WeeklyAnimeRes;
import java.util.List;

public interface WeeklyAnimeService {
    List<WeeklyAnimeRes> getWeeklyAnime();
}
