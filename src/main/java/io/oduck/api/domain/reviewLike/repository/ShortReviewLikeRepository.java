package io.oduck.api.domain.reviewLike.repository;

import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortReviewLikeRepository extends JpaRepository<ShortReviewLike,Long> {

    Optional<ShortReviewLike> findByMemberIdAndShortReviewId(Long memberId, Long shortReviewId);

}