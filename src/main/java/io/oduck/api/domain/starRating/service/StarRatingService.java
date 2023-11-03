package io.oduck.api.domain.starRating.service;

public interface StarRatingService {
    boolean createScore(Long memberId, Long animeId, int score);
}
