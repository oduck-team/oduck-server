package io.oduck.api.domain.review.service;

import static io.oduck.api.global.utils.PagingUtils.applyPageableForNonOffset;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberProfileRepository;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDsl;
import io.oduck.api.domain.review.dto.ShortReviewReqDto;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewCountRes;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortReviewServiceImpl implements ShortReviewService{

    private final ShortReviewRepository shortReviewRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberRepository memberRepository;

    private final AnimeRepository animeRepository;


    @Override
    @Transactional
    public void save(Long memberId, PostShortReviewReq shortReviewReq) {
        ShortReview shortReview = ShortReview
                                      .builder()
                                      .content(shortReviewReq.getContent())
                                      .hasSpoiler(shortReviewReq.isHasSpoiler())
                                      .build();

        //애니 입력
        Anime anime = animeRepository.findByIdForUpdate(shortReviewReq.getAnimeId())
                             .orElseThrow(
                                 () -> new NotFoundException("Anime")
                             );
        shortReview.relateAnime(anime);

        //회원 입력
        Member member = memberRepository.findById(memberId)
                            .orElseThrow(
                                () -> new NotFoundException("Memebr")
                            );
        shortReview.relateMember(member);

        ShortReview saveShortReview = shortReviewRepository.save(shortReview);
        anime.increaseReviewCount();

        //log.info("ShortReview Crated! {}", saveShortReview.getId());
    }

    @Override
    public SliceResponse<ShortReviewRes> getShortReviews(Long animeId, String cursor,ShortReviewReqDto.Sort sort, OrderDirection order, int size) {
        Sort sortList = Sort.by(
            Direction.fromString(order.getOrder()),
            sort.getSort()
        );

        if(sort == ShortReviewReqDto.Sort.LIKE){
            sortList = sortList.and(Sort.by(Direction.DESC, "createdAt"));
        }else if(sort == ShortReviewReqDto.Sort.SCORE){
            sortList = sortList.and(Sort.by(Direction.DESC, "createdAt"));
        }

        Slice<ShortReviewDsl> shortReviews = shortReviewRepository.selectShortReviews(
            animeId,
            cursor,
            applyPageableForNonOffset(
                size,
                sortList
            )
        );

        List<ShortReviewRes> res = shortReviews.getContent()
                                       .stream()
                                       .map(ShortReviewRes::of)
                                       .toList();

        return SliceResponse.of(shortReviews, res, sort.getSort());
    }

    @Override
    public ShortReviewCountRes getShortReviewCountByMemberId(Long memberId) {
        Long count = shortReviewRepository.countByMemberId(memberId);
        return ShortReviewCountRes.builder()
                   .count(count)
                   .build();
    }

    @Override
    public void update(Long memberId, Long reviewId, PatchShortReviewReq req) {
        ShortReview findShortReview = getShortReview(reviewId);
        Long findMemberId = findShortReview.getMember().getId();
        //리뷰 작성자 인지 확인
        Optional
            .ofNullable(findMemberId)
            .ifPresent(
                id -> {
                    if(!findMemberId.equals(memberId)) {
                        throw new BadRequestException("Not the author of the review.");
                    }
                    findShortReview.updateContent(req.getContent());
                    findShortReview.updateSpoiler(req.isHasSpoiler());
                }
            );
    }


    private ShortReview getShortReview(Long reviewId){
        return shortReviewRepository.findById(reviewId)
                   .orElseThrow(
                       () -> new NotFoundException("shortReview")
                   );
    }
}