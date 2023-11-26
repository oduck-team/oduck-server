package io.oduck.api.domain.reviewLike.service;

import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto.ShortReviewLikeReq;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeResDto.IsLikeRes;
import io.oduck.api.domain.reviewLike.entity.ShortReviewLike;
import io.oduck.api.domain.reviewLike.repository.ShortReviewLikeRepository;
import io.oduck.api.global.exception.NotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShortReviewLikeServiceImpl implements ShortReviewLikeService{

    final private ShortReviewLikeRepository shortReviewLikeRepository;
    final private ShortReviewRepository shortReviewRepository;
    final private MemberRepository memberRepository;

    @Override
    public Boolean postLike(Long memberId, ShortReviewLikeReq likeRes) {
        //리뷰 좋아요 작성 여부 확인 후 없으면 생성 있으면 삭제 리뷰 찾기 회원 찾기
        Optional<ShortReviewLike> optionalLike = getShortReviewLike(memberId, likeRes.getShortReviewId());
        if(optionalLike.isPresent()){
            shortReviewLikeRepository.delete(optionalLike.get());
            return false;
        }
        ShortReview shortReview = shortReviewRepository.findById(likeRes.getShortReviewId())
                                      .orElseThrow(() -> new NotFoundException("ShortReview"));

        Member member = memberRepository.findById(memberId)
                            .orElseThrow(() -> new NotFoundException("Member"));

        ShortReviewLike like = ShortReviewLike
                                   .builder()
                                   .shortReview(shortReview)
                                   .member(member)
                                   .build();

        shortReviewLikeRepository.save(like);
        return true;
    }

    @Override
    public IsLikeRes checkReviewLike(Long shortReviewId, Long memberId) {
        Optional<ShortReviewLike> optionalLike = getShortReviewLike(memberId, shortReviewId);
        return IsLikeRes
                   .builder()
                   .isLike(optionalLike.isPresent())
                   .build();
    }

    private Optional<ShortReviewLike> getShortReviewLike(Long memberId, Long shortReviewId){
        return shortReviewLikeRepository.findByMemberIdAndShortReviewId(memberId,shortReviewId);
    }
}