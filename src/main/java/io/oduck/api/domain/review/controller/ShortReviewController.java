package io.oduck.api.domain.review.controller;

import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.service.ShortReviewService;
import io.oduck.api.global.common.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/short-review")
@RestController
@RequiredArgsConstructor
public class ShortReviewController {

    private final ShortReviewService shortReviewService;

    //애니의 짧은 리뷰 조회
    @GetMapping("/{animeId}")
    public ResponseEntity<?> getShortReviews(
        @PathVariable Long animeId)  {
        //TODO: 애니에 따른 짧은 리뷰 조회
        ShortReviewResDto reviewResDto  = shortReviewService.getShortReviews(animeId);
        return ResponseEntity.ok(SingleResponse.of(reviewResDto));
    }

    //TODO :

}