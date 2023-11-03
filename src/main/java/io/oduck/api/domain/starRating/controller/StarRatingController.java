package io.oduck.api.domain.starRating.controller;

import io.oduck.api.domain.starRating.dto.StarRatingReqDto.CreateReq;
import io.oduck.api.domain.starRating.service.StarRatingService;
import io.oduck.api.global.security.auth.dto.AuthUser;
import io.oduck.api.global.security.auth.dto.LoginUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
@RequestMapping("/ratings")
@RequiredArgsConstructor
@RestController
public class StarRatingController {
    private final StarRatingService starRatingService;

    @PostMapping("/{animeId}")
    public ResponseEntity<?> PostScore(
        @PathVariable("animeId") @Positive Long animeId,
        @RequestBody @Valid CreateReq body,
        @LoginUser AuthUser user
    ) {
        boolean res = starRatingService.createScore(user.getId(), animeId, body.getScore());
        return ResponseEntity.status(res ? HttpStatus.CREATED : HttpStatus.CONFLICT).build();
    }

    @GetMapping("/{animeId}")
    public ResponseEntity<?> GetScore(
        @PathVariable("animeId") @Positive Long animeId,
        @LoginUser AuthUser user
    ) {
        return ResponseEntity.ok(starRatingService.chekRated(user.getId(), animeId));
    }
}
