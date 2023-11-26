package io.oduck.api.domain.review.repository;

import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShortReviewRepositoryCustom {

    Slice<ShortReviewDsl> selectShortReviews(Long animeId, String cursor, Pageable pageable);
    Slice<ShortReviewDsl> selectShortReviewsByMemberId(Long memberId, String cursor, Pageable pageable);
}
