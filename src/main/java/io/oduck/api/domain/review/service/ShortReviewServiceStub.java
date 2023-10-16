package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.dto.ShortReviewResDto.MemberProfile;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReview;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShortReviewServiceStub implements ShortReviewService{

    @Override
    public ShortReviewResDto getShortReviews(Long animeId) {
        List<ShortReview> shortReviewList = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            ShortReview shortReview =  createReview(animeId);
            shortReviewList.add(shortReview);
        }
        //slice객체 만들기
        PageRequest pageable = PageRequest.of(0,10);
        Slice<ShortReview> slice = new SliceImpl<>(shortReviewList,pageable,true);

        return ShortReviewResDto
                   .builder()
                   .shortReviews(slice)
                   .build();
    }

    @Override
    public void save(PostShortReviewReq shortReviewReq) {

    }

    @Override
    public void update(Long reviewId, PatchShortReviewReq req) {

    }

    private ShortReview createReview(Long animeId){

        return ShortReview
                   .builder()
                   .reviewId(1L)
                   .animeId(1L)
                   .content("최고의 반전의 반전")
                   .score(5)
                   .hasSpoiler(true)
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
