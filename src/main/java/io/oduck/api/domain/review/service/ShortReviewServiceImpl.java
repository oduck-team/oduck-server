package io.oduck.api.domain.review.service;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.NotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortReviewServiceImpl implements ShortReviewService{

    private final ShortReviewRepository shortReviewRepository;
    private final MemberRepository memberRepository;
    private final AnimeRepository animeRepository;

    @Override
    public ShortReviewResDto getShortReviews(Long anime) {
        return null;
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

        Optional
            .ofNullable(saveShortReview.getContent())
            .ifPresent(
                content ->{
                    if(saveShortReview.getContent().equals(shortReview.getContent())){
                        throw new BadRequestException("Failed to saveFail.");
                    }
                    //리뷰 작성 성공시 애니 리뷰카운트 증가
                    anime.increaseReviewCount();
                }
            );
        log.info("ShortReview Crated! {}", saveShortReview.getId());
    }

    @Override
    public void update(Long reviewId, PatchShortReviewReq req) {
        ShortReview findShortReview = getShortReview(reviewId);

        Optional
            .ofNullable(req.getContent())
                .ifPresent(
                    content ->{
                        if(findShortReview.getContent().equals(req.getContent())){
                            throw new BadRequestException("Same content as before.");
                        }
                        findShortReview.updateContent(req.getContent());
                    }
                );

        Optional
            .ofNullable(req.getContent())
            .ifPresent(
                hasSpoiler ->{
                    if(findShortReview.isHasSpoiler() == req.isHasSpoiler()){
                        throw new BadRequestException("Same spoiler as before.");
                    }
                    findShortReview.updateSpoiler(req.isHasSpoiler());
                }
            );
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