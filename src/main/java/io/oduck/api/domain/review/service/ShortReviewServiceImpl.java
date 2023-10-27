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
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortReviewServiceImpl implements ShortReviewService{

    private final ShortReviewRepository shortReviewRepository;
    private final MemberProfileRepository memberProfileRepository;
    private final MemberRepository memberRepository;

    private final AnimeRepository animeRepository;

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
    public void save(Long memberId, PostShortReviewReq shortReviewReq) {
        ShortReview shortReview = ShortReview
                                      .builder()
                                      .content(shortReviewReq.getContent())
                                      .hasSpoiler(shortReviewReq.isHasSpoiler())
                                      .build();

        //애니 입력
        Anime anime = getAnime(shortReviewReq.getAnimeId());
        shortReview.relateAnime(anime);

        //회원 입력
        shortReview.relateMember(getMember(memberId));

        ShortReview saveShortReview = shortReviewRepository.save(shortReview);
        anime.increaseReviewCount();

        //log.info("ShortReview Crated! {}", saveShortReview.getId());
    }

    @Override
    public void update(Long reviewId, PatchShortReviewReq req) {
        ShortReview findShortReview = getShortReview(reviewId);
        findShortReview.updateContent(req.getContent());
        findShortReview.updateSpoiler(req.isHasSpoiler());
        shortReviewRepository.save(findShortReview);
    }

    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                  .orElseThrow(
                      () -> new NotFoundException("Member")
                  );
    }

    private Anime getAnime(Long animeId){
        return animeRepository.findById(animeId)
                   .orElseThrow(
                       () -> new NotFoundException("Anime")
                   );
    }

    private ShortReview getShortReview(Long reviewId){
        return shortReviewRepository.findById(reviewId)
                   .orElseThrow(
                       () -> new NotFoundException("shortReview")
                   );
    }
}