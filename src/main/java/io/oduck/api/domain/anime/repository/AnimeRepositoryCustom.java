package io.oduck.api.domain.anime.repository;

import io.oduck.api.domain.admin.dto.AdminReq.QueryType;
import io.oduck.api.domain.admin.dto.AdminReq.SearchFilter;
import io.oduck.api.domain.admin.dto.AdminRes;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.global.common.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;

public interface AnimeRepositoryCustom {
    Slice<SearchResult> findSliceByCondition(String query, String cursor, Pageable pageable, SearchFilterDsl searchFilterDsl);

    PageResponse<AdminRes.SearchResult> findPageByCondition(String query, QueryType queryType,
        Pageable pageable, SearchFilter searchFilter);
}
