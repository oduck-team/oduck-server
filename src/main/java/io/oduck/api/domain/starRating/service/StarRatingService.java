package io.oduck.api.domain.starRating.service;

import io.oduck.api.domain.starRating.dto.StarRatingResDto.RatedRes;

public interface StarRatingService {
    boolean createScore(Long memberId, Long animeId, int score);
    RatedRes checkRated(Long memberId, Long animeId);
    boolean updateScore(Long memberId, Long animeId, int score);
}
