package io.oduck.api.domain.starRating.service;

import io.oduck.api.domain.starRating.dto.StarRatingResDto.RatedDateTimeRes;

public interface StarRatingService {
    boolean createScore(Long memberId, Long animeId, int score);
    RatedDateTimeRes checkRated(Long memberId, Long animeId);
}
