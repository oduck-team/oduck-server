package io.oduck.api.domain.starRating.repository;

import io.oduck.api.domain.starRating.entity.StarRating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRatingRepository extends JpaRepository<StarRating, Long> {
    Optional<StarRating> findByMemberIdAndAnimeId(Long memberId, Long animeId);
}
