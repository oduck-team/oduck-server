package io.oduck.api.domain.genre.repository;

import io.oduck.api.domain.genre.entity.Genre;
import java.util.List;

public interface GenreRepositoryCustom {

    List<Genre> findAllByAnimeId(Long animeId);
    boolean existsByName(String name);
}
