package io.oduck.api.domain.review.controller;

import io.oduck.api.domain.attractionPoint.dto.AttractionPointResDto.IsAttractionPoint;
import io.oduck.api.domain.attractionPoint.service.AttractionPointService;
import io.oduck.api.domain.review.dto.ShortReviewReqDto;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.Sort;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.domain.review.service.ShortReviewService;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/short-reviews")
@RestController
@RequiredArgsConstructor
public class ShortReviewController {

    private final ShortReviewService shortReviewService;
    private final AttractionPointService attractionPointService;

    @PostMapping
    public ResponseEntity<?> postShortReview(
        @LoginUser AuthUser user,
        @RequestBody @Valid ShortReviewReqDto.ShortReviewReq req)  {
        //TODO : 짧은 리뷰 작성
        return ResponseEntity.ok().build();
    }

    @GetMapping("/animes/{animeId}")
    public ResponseEntity<?> getShortReviews(
        @PathVariable Long animeId,
        @RequestParam(required = false) String cursor,
        @RequestParam(required = false, defaultValue = "created_at") Sort sort,
        @RequestParam(required = false, defaultValue = "DESC") OrderDirection order,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) int size
    )  {
        //TODO: 애니에 따른 짧은 리뷰 조회
        SliceResponse<ShortReviewRes> reviewRes = shortReviewService.getShortReviews(animeId, cursor, sort, order, size);
        return ResponseEntity.ok(reviewRes);
    }
    @GetMapping("/attraction-points")
    public ResponseEntity<?> patchShortReview(
        @LoginUser AuthUser user,
        @RequestParam(required = false) Long animeId,
        @RequestParam(required = false) String name)  {
        //TODO : 짧은 리뷰 수정, 입덕포인트 반환
        IsAttractionPoint attractionPointRes = attractionPointService.isAttractionPoint(user.getId(), animeId);
        return ResponseEntity.ok(attractionPointRes);
    }

      @PatchMapping("/{reviewId}")
    public ResponseEntity<?> patchShortReview(
        @LoginUser AuthUser user,
        @PathVariable Long reviewId,
        @RequestBody @Valid ShortReviewReqDto.ShortReviewReq req)  {
        //TODO : 짧은 리뷰 수정
        shortReviewService.update(user.getId(), reviewId, req);
        return ResponseEntity.noContent().build();
    }
}