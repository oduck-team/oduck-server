package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.global.common.OrderDirection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static io.oduck.api.domain.anime.dto.AnimeReq.Sort;
import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;

public interface AnimeRepositoryCustom {
    Slice<SearchResult> findAnimesByCondition(String query, String cursor, Sort sort, OrderDirection order, Pageable pageable, SearchFilterDsl searchFilterDsl);
}
