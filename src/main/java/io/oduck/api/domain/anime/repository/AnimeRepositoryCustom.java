package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;

public interface AnimeRepositoryCustom {
    Slice<SearchResult> findAnimesByCondition(String query, String cursor, Pageable pageable, SearchFilterDsl searchFilterDsl);
}
