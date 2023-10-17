package io.oduck.api.domain.review.repository;

import io.oduck.api.domain.review.entity.ShortReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortReviewRepository extends JpaRepository<ShortReview,Long> {

}
