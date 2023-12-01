package io.oduck.api.domain.review.repository;

import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDslWithTitle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ShortReviewRepositoryCustom {

    Slice<ShortReviewDsl> selectShortReviews(Long animeId, String cursor, Pageable pageable);
    Slice<ShortReviewDslWithTitle> selectShortReviewsByMemberId(Long memberId, String cursor, Pageable pageable);
}
