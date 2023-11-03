
package io.oduck.api.domain.reviewLike.controller;

import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeResDto;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto.ShortReviewLikeReq;
import io.oduck.api.domain.reviewLike.service.ShortReviewLikeService;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class ShortReviewLikeController {

    final private ShortReviewLikeService shortReviewLikeService;

    @PostMapping
    public ResponseEntity<?> postLike(
        @LoginUser AuthUser user,
        @RequestBody @Valid ShortReviewLikeReq likeRes
    ){
        //TODO: 리뷰 좋아요 생성, 존재하지 않다면 생성, 존재한다면 삭제
        boolean postLike = shortReviewLikeService.postLike(user.getId(), likeRes);
        return ResponseEntity.status(postLike ? HttpStatus.CREATED : HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{shortReviewId}")
    public ResponseEntity<?> getLike(
        @PathVariable Long shortReviewId,
        @LoginUser AuthUser user
    ){
        //TODO: 좋아요한 리뷰인지 확인
        return ResponseEntity.ok(shortReviewLikeService.checkReviewLike(shortReviewId, user.getId()));
    }

}