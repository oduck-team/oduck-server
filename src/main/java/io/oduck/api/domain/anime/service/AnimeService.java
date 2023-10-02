package io.oduck.api.domain.anime.service;

import io.oduck.api.domain.anime.dto.AnimeRes;

public interface AnimeService {

    /**
     * 애니 상세 조회 로직
     * @param animeId
     * @return AnimeRes
     */
    AnimeRes getAnimeById(Long animeId);
}
