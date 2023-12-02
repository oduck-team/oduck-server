package io.oduck.api.domain.weekly.service;

import io.oduck.api.domain.genre.entity.Genre;
import io.oduck.api.domain.genre.repository.GenreRepository;
import io.oduck.api.domain.weekly.dto.WeeklyDsl.WeeklyAnimeDsl;
import io.oduck.api.domain.weekly.dto.WeeklyRes;
import io.oduck.api.domain.weekly.dto.WeeklyRes.WeeklyAnimeRes;
import io.oduck.api.domain.weekly.repository.WeeklyAnimeRepository;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklyAnimeServiceImpl implements WeeklyAnimeService{
    private final WeeklyAnimeRepository weeklyAnimeRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<WeeklyAnimeRes> getWeeklyAnime() {
        List<WeeklyAnimeDsl> animeDsls = weeklyAnimeRepository.getWeeklyAnime();
        List<WeeklyAnimeRes> animeRes = animeDsls.stream().map(WeeklyAnimeRes::of).toList();
        for (WeeklyAnimeRes a : animeRes) {
            a.withGenres(
                genreRepository.findAllByAnimeId(a.getAnimeId()).stream().map(Genre::getName)
                    .toList()
            );
        }
        return animeRes;
    }
}
