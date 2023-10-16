package io.oduck.api.domain.reviewLike.repository;

import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortReviewLikeRepository extends JpaRepository<ShortReviewLike,Long> {

}
