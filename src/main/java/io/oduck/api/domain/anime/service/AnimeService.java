package io.oduck.api.domain.anime.service;

import static io.oduck.api.domain.anime.dto.AnimeReq.PatchAnimeReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchGenreIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchOriginalAuthorIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchSeriesIdReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchStudioIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PatchVoiceActorIdsReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.PostReq;
import static io.oduck.api.domain.anime.dto.AnimeReq.Sort;
import static io.oduck.api.domain.anime.dto.AnimeRes.DetailResult;
import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;

import io.oduck.api.domain.admin.dto.AdminReq;
import io.oduck.api.domain.admin.dto.AdminReq.QueryType;
import io.oduck.api.domain.admin.dto.AdminReq.SearchFilter;
import io.oduck.api.domain.admin.dto.AdminRes;
import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.PageResponse;
import io.oduck.api.global.common.SliceResponse;

public interface AnimeService {

    /**
     * 애니 저장 로직이다.
     */
    void save(PostReq req);

    /**
     * 애니 상세 조회 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @return DetailResult 애니 응답 dto;
     */
    DetailResult getAnimeById(Long animeId);

    /**
     * 애니 검색 결과 조회이다.
     * @param query 제목 검색 내용;
     * @param cursor 마지막 애니의 제목;
     * @param sort 정렬 기준;
     * @param order 정렬 방향;
     * @param size 불러올 검색 결과 크기;
     * @param searchFilterDsl 검색 필터 dto;
     * @return SliceResponse non-offset 페이징 dto
     */
    SliceResponse<SearchResult> getSliceByCondition(String query, String cursor, Sort sort, OrderDirection order, int size, SearchFilterDsl searchFilterDsl);

    /**
     * 애니 검색 결과 조회이다.
     *
     * @param query        제목 검색 내용;
     * @param queryType    검색의 타입이며 타입에 따라 쿼리의 질의가 다름;
     * @param page         현재 페이지의 번호;
     * @param size         하나의 페이지에 포함될 콘텐츠 사이즈;
     * @param sort
     * @param order
     * @param searchFilter 검색 필터;
     * @return PageResponse offset 페이징 dto
     */
    PageResponse<AdminRes.SearchResult> getPageByCondition(String query, QueryType queryType, int page, int size,
        AdminReq.Sort sort, OrderDirection order, SearchFilter searchFilter);

    /**
     * 애니 수정 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @param patchReq 애니 수정 dto;
     */
    void update(Long animeId, PatchAnimeReq patchReq);

    /**
     * 애니 원작 작가 수정 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @param patchReq 애니 원작 작가 수정 dto;
     */
    void updateAnimeOriginalAuthors(Long animeId, PatchOriginalAuthorIdsReq patchReq);

    /**
     * 애니 스튜디오 수정 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @param patchReq 애니 스튜디오 수정 dto;
     */
    void updateAnimeStudios(Long animeId, PatchStudioIdsReq patchReq);

    /**
     * 애니 성우 수정 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @param patchReq 애니 성우 수정 dto;
     */
    void updateAnimeVoiceActors(Long animeId, PatchVoiceActorIdsReq patchReq);

    /**
     * 애니 장르 수정 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @param patchReq 애니 장르 수정 dto;
     */
    void updateAnimeGenres(Long animeId, PatchGenreIdsReq patchReq);

    /**
     * 애니 시리즈 수정 로직이다.
     * @param animeId 애니의 고유 식별자;
     * @param patchReq 애니 시리즈 수정 dto;
     */
    void updateSeries(Long animeId, PatchSeriesIdReq patchReq);

    /**
     * 애니 삭제 로직이다.
     * @param animeId 애니의 고유 식별자;
     */
    void delete(Long animeId);
}
