package io.oduck.api.domain.review.controller;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import io.oduck.api.domain.attractionPoint.service.AttractionPointService;
import io.oduck.api.domain.review.dto.ShortReviewReqDto;
import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.service.ShortReviewService;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.security.auth.dto.LoginUser;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/short-reviews")
@RestController
@RequiredArgsConstructor
public class ShortReviewController {

    private final ShortReviewService shortReviewService;
    private final AttractionPointService attractionPointService;

    //애니의 짧은 리뷰 조회
    @GetMapping("/{animeId}")
    public ResponseEntity<?> getShortReviews(
        @PathVariable Long animeId)  {
        //TODO: 애니에 따른 짧은 리뷰 조회
        ShortReviewResDto reviewResDto = shortReviewService.getShortReviews(animeId);
        return ResponseEntity.ok(SliceResponse.of(reviewResDto.getShortReviews()));
    }
    @PostMapping
    public ResponseEntity<?> postShortReview(
        @LoginUser AuthLocal user,
        @RequestBody @Valid ShortReviewReqDto.PostShortReviewReq req)  {
        //TODO : 짧은 리뷰 작성
        shortReviewService.save(user.getId(), req);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<?> patchShortReview(
        @LoginUser AuthLocal user,
        @PathVariable Long reviewId,
        @RequestBody @Valid ShortReviewReqDto.PatchShortReviewReq req)  {
        //TODO : 짧은 리뷰 수정
        shortReviewService.update(reviewId, req);
        //입덕포인트 반환
        IsAttractionPoint attractionPointRes = attractionPointService.isAttractionPoint(user.getId(), req.getAnimeId());
        return ResponseEntity.ok(attractionPointRes);
    }
}