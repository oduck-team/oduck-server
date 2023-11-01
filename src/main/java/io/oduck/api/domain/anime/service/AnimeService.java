package io.oduck.api.domain.anime.service;

import io.oduck.api.domain.anime.dto.SearchFilterDsl;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;

import static io.oduck.api.domain.anime.dto.AnimeReq.*;
import static io.oduck.api.domain.anime.dto.AnimeRes.DetailResult;
import static io.oduck.api.domain.anime.dto.AnimeRes.SearchResult;

public interface AnimeService {

    /**
     * 애니 상세 조회 로직
     * @param animeId
     * @return AnimeRes
     */
    DetailResult getAnimeById(Long animeId);

    /**
     * 애니 저장 로직
     * @return AnimeId
     */
    void save(PostReq req);

    /**
     * 애니 수정 로직
     * @param animeId
     * @param req
     */
    void update(Long animeId, PatchAnimeReq req);

    /**
     * 애니 원작 작가 수정 로직
     * @param animeId
     * @param originalAuthors
     */
    void updateAnimeOriginalAuthors(Long animeId, PatchOriginalAuthorIdsReq originalAuthors);

    /**
     * 애니 스튜디오 수정 로직
     * @param animeId
     * @param patchReq
     */
    void updateAnimeStudios(Long animeId, PatchStudioIdsReq patchReq);

    /**
     * 애니 성우 수정 로직
     * @param animeId
     * @param patchReq
     */
    void updateAnimeVoiceActors(Long animeId, PatchVoiceActorIdsReq patchReq);

    /**
     * 애니 장르 수정 로직
     * @param animeId
     * @param patchReq
     */
    void updateAnimeGenres(Long animeId, PatchGenreIdsReq patchReq);

    /**
     * 애니 시리즈 수정 로직
     * @param animeId
     * @param patchReq
     */
    void updateSeries(Long animeId, PatchSeriesIdReq patchReq);

    /**
     * 애니 검색 결과 조회
     * @param query
     * @param cursor
     * @param sort
     * @param order
     * @param size
     * @param searchFilterDsl
     * @return SliceResponse<SearchResult>
     */
    SliceResponse<SearchResult> getAnimesByCondition(String query, String cursor, Sort sort, OrderDirection order, int size, SearchFilterDsl searchFilterDsl);
}
