package io.oduck.api.domain.review.repository;

import io.oduck.api.domain.review.entity.ShortReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortReviewRepository extends JpaRepository<ShortReview,Long>, ShortReviewRepositoryCustom {
    Optional<ShortReview> findByIdAndDeletedAtIsNull(Long memberId);

    Long countByMemberIdAndDeletedAtIsNull(Long memberId);
}
