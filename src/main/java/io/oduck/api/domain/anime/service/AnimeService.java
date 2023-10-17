package io.oduck.api.domain.anime.service;

import io.oduck.api.domain.anime.dto.AnimeReq.*;
import io.oduck.api.domain.anime.dto.AnimeRes;
import io.oduck.api.domain.anime.entity.AnimeGenre;
import io.oduck.api.domain.anime.entity.AnimeOriginalAuthor;
import io.oduck.api.domain.anime.entity.AnimeStudio;
import io.oduck.api.domain.anime.entity.AnimeVoiceActor;
import io.oduck.api.domain.series.entity.Series;
import java.util.List;

public interface AnimeService {

    /**
     * 애니 상세 조회 로직
     * @param animeId
     * @return AnimeRes
     */
    AnimeRes getAnimeById(Long animeId);

    /**
     * 애니 저장 로직
     * @param req
     * @return AnimeId
     */
    Long save(PostReq req);

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
    void updateAnimeOriginalAuthors(Long animeId, List<AnimeOriginalAuthor> originalAuthors);

    /**
     * 애니 스튜디오 수정 로직
     * @param animeId
     * @param animeStudios
     */
    void updateAnimeStudios(Long animeId, List<AnimeStudio> animeStudios);

    /**
     * 애니 성우 수정 로직
     * @param animeId
     * @param animeVoiceActors
     */
    void updateAnimeVoiceActors(Long animeId, List<AnimeVoiceActor> animeVoiceActors);

    /**
     * 애니 장르 수정 로직
     * @param animeId
     * @param animeGenres
     */
    void updateAnimeGenres(Long animeId, List<AnimeGenre> animeGenres);

    /**
     * 애니 시리즈 수정 로직
     * @param animeId
     * @param series
     */
    void update(Long animeId, Series series);
}
