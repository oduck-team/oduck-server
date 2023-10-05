package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.dto.ShortReviewResDto.MemberProfile;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReview;
import org.springframework.stereotype.Service;

@Service
public class ShortReviewServiceStub implements ShortReviewService{

    @Override
    public ShortReviewResDto getShortReviews(Long animeId) {
        ShortReview shortReview = createReview(animeId);

        return ShortReviewResDto
                   .builder()
                   .shortReview(shortReview)
                   .build();
    }

    private ShortReview createReview(Long animeId){
        return ShortReview
                   .builder()
                   .animeId(1L)
                   .title("이세계 용사")
                   .content("최고의 반전의 반전")
                   .score(5)
                   .hasSpoiler(false)
                   .shortReviewLikeCount(100)
                   .member(getMemberProfile())
                   .build();
    }

    private MemberProfile getMemberProfile(){
        return MemberProfile.builder()
                   .name("오덕12")
                   .thumbnail("사진 url")
                   .build();
    }
}